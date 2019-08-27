package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.app.Dialog
import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ftinc.kit.kotlin.utils.bindParcelable
import com.ftinc.kit.kotlin.utils.bundle
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Action
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Source
import kotlinx.android.synthetic.main.fragment_attack_dialog.*

class AttackDialogFragment : DialogFragment() {

    interface AttackListener {

        fun onAttack(actions: List<Action>)
    }

    var attackListener: AttackListener? = null

    private val targetPlayer by bindParcelable<Board.Player>(KEY_TARGET_PLAYER)
    private var damage: Int = 0
        set(value) {
            field = value.coerceAtLeast(0)
            attackDamage.text = "$field"
        }
    private var benchAttackDamage = SparseIntArray(5)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attack_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), R.layout.item_playtest_action, resources.getStringArray(R.array.status_effects))
        adapter.setDropDownViewResource(R.layout.item_playtest_action)
        statusSpinner.adapter = adapter
        statusSpinner.setSelection(when (targetPlayer.active?.statusEffect) {
            Board.Card.Status.SLEEPING -> 1
            Board.Card.Status.CONFUSED -> 2
            Board.Card.Status.PARALYZED -> 3
            else -> 0
        })

        burnedSwitch.isChecked = targetPlayer.active?.isBurned ?: false
        poisonSwitch.isChecked = targetPlayer.active?.isPoisoned ?: false

        // Fill out bench, if possible
        targetPlayer.bench.cards.forEach { (position, card) ->
            card.pokemons.firstOrNull()?.let { pokemon ->
                val itemView = layoutInflater.inflate(R.layout.item_playtest_action_bench, benchContainer, false)
                itemView.tag = position

                val image = itemView.findViewById<ImageView>(R.id.image)
                val name = itemView.findViewById<TextView>(R.id.name)
                val actionSubtractDamage = itemView.findViewById<ImageView>(R.id.actionSubtract)
                val benchDamage = itemView.findViewById<TextView>(R.id.attackDamage)
                val actionAddDamage = itemView.findViewById<ImageView>(R.id.actionAdd)

                GlideApp.with(this)
                        .load(pokemon.imageUrl)
                        .placeholder(R.drawable.pokemon_card_back)
                        .into(image)

                name.text = pokemon.name
                actionAddDamage.setOnClickListener {
                    benchAttackDamage.put(position, benchAttackDamage[position, 0] + DAMAGE_AMOUNT)
                    benchDamage.text = "${benchAttackDamage[position, 0]}"
                }

                actionSubtractDamage.setOnClickListener {
                    benchAttackDamage.put(position, (benchAttackDamage[position, 0] - DAMAGE_AMOUNT).coerceAtLeast(0))
                    benchDamage.text = "${benchAttackDamage[position, 0]}"
                }

                benchContainer.addView(itemView)
            }
        }

        // Listeners
        actionAdd.setOnClickListener {
            damage += DAMAGE_AMOUNT
        }

        actionSubtract.setOnClickListener {
            damage -= DAMAGE_AMOUNT
        }

        actionPositive.setOnClickListener {
            applyAttackAndBuildActions()
            dismiss()
        }

        actionNegative.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private fun applyAttackAndBuildActions() {
        val actions = mutableListOf<Action>()

        // 1. Set damage action
        actions += Action.Actor.Damage(targetPlayer.type, Source.Active, damage)

        // 2. Set Status and Conditions
        actions += Action.Actor.Active.Condition(targetPlayer.type, burnedSwitch.isChecked, poisonSwitch.isChecked)
        actions += Action.Actor.Active.Status(targetPlayer.type, when (statusSpinner.selectedItemPosition) {
            1 -> Board.Card.Status.SLEEPING
            2 -> Board.Card.Status.CONFUSED
            3 -> Board.Card.Status.PARALYZED
            else -> null
        })

        // 3. Apply Bench changes
        for (i in (0 until benchAttackDamage.size())) {
            val key = benchAttackDamage.keyAt(i)
            val damage = benchAttackDamage.valueAt(i)

            if (damage > 0) {
                actions += Action.Actor.Damage(targetPlayer.type, Source.Bench(key), damage)
            }
        }

        attackListener?.onAttack(actions)
    }

    companion object {
        const val TAG = "AttackDialogFragment"
        private const val KEY_TARGET_PLAYER = "AttackDialogFragment.TargetPlayer"
        private const val DAMAGE_AMOUNT = 10

        fun show(fragmentManager: FragmentManager, targetPlayer: Board.Player): AttackDialogFragment {
            return AttackDialogFragment().apply {
                arguments = bundle {
                    KEY_TARGET_PLAYER to targetPlayer
                }
                show(fragmentManager, TAG)
            }
        }
    }
}
