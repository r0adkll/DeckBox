package com.r0adkll.deckbuilder.arch.ui.features.playtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Action
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.arch.ui.features.playtest.actions.*
import com.r0adkll.deckbuilder.arch.ui.features.playtest.actions.ActionBottomSheetFragment.Companion.dismissActionSheet
import com.r0adkll.deckbuilder.arch.ui.features.playtest.di.PlaytestModule
import com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets.BoardCardView
import com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets.BoardView
import com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets.CardStackView
import com.r0adkll.deckbuilder.util.extensions.toast
import kotlinx.android.synthetic.main.activity_playtest_simulator.*
import timber.log.Timber
import javax.inject.Inject


class PlaytestActivity : BaseActivity(), PlaytestUi, PlaytestUi.Intentions, PlaytestUi.Actions,
        ActionBottomSheetFragment.MenuItemListener,
        BoardView.BoardListener,
        CounterBottomSheetFragment.CounterListener {

    override var state: PlaytestUi.State = PlaytestUi.State.DEFAULT

    @Inject lateinit var presenter: PlaytestPresenter
    @Inject lateinit var renderer: PlaytestRenderer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playtest_simulator)

        playmat.setBoardListener(this)
    }


    override fun setupComponent() {
        DeckApp.component.plus(PlaytestModule(this))
                .inject(this)
    }


    override fun render(state: PlaytestUi.State) {
        this.state = state
        renderer.render(state)

//        addDelegate(RendererActivityDelegate(renderer))
//        addDelegate(PresenterActivityDelegate(presenter))
    }


    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showError(description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
     * Board and UI element listeners surrounding the gameplay
     */

    override fun onCardStackClicked(view: CardStackView) {
        view.debug = false
        val lp = view.layoutParams as? BoardView.LayoutParams
        Timber.i("Card Stack Clicked (${lp?.playerType?.name}, ${lp?.element?.name}) = ${view.cards.size}")
        if (lp?.element == BoardView.BoardElement.DECK) {
            ActionBottomSheetFragment.show(supportFragmentManager, ActionSheet.DECK)
        }
    }

    override fun onCardClicked(view: BoardCardView) {
        val lp = view.layoutParams as? BoardView.LayoutParams
        Timber.i("Card Clicked (${lp?.playerType?.name}, ${lp?.element?.name}) = ${view.card?.pokemons?.firstOrNull()?.id}")
        if (lp?.element == BoardView.BoardElement.ACTIVE) {
            ActionBottomSheetFragment.show(supportFragmentManager, ActionSheet.ACTIVE)
        }
    }

    override fun onCardDropped(view: BoardCardView, targetType: BoardView.BoardElement, targetElement: BoardView.Element): Boolean {
        val lp = view.layoutParams as? BoardView.LayoutParams
        toast("Card Dropped on (${targetType.name}, element=${targetElement::class.java.simpleName}) - (${lp?.playerType?.name}, ${lp?.element?.name}) = ${view.card?.pokemons?.firstOrNull()?.id}")
        Timber.i("Card Dropped on (${targetType.name}, element=${targetElement::class.java.simpleName}) - (${lp?.playerType?.name}, ${lp?.element?.name}) = ${view.card?.pokemons?.firstOrNull()?.id}")
        return false
    }

    override fun onBoardElementClicked(playerType: Board.Player.Type, elementType: BoardView.BoardElement, element: BoardView.Element) {
        toast("Element Clicked (${playerType.name}, ${elementType.name}, ${element::class.java.simpleName})")
        Timber.i("Element Clicked (${playerType.name}, ${elementType.name}, ${element::class.java.simpleName})")
    }

    override fun onBoardElementLongClicked(playerType: Board.Player.Type, elementType: BoardView.BoardElement, element: BoardView.Element) {
        toast("Element Long Clicked (${playerType.name}, ${elementType.name}, ${element::class.java.simpleName})")
        Timber.i("Element Long Clicked (${playerType.name}, ${elementType.name}, ${element::class.java.simpleName})")
    }

    override fun onMenuItemActionClicked(sheet: ActionSheet, item: MenuItem) {
        supportFragmentManager.dismissActionSheet()
        if (item.id == 4 && sheet.id == ActionSheet.ACTIVE_ID) {
            CounterBottomSheetFragment.show(
                    supportFragmentManager,
                    R.string.dialog_counter_title_damage,
                    R.string.action_apply,
                    android.R.string.cancel
            )
        } else if (item.id == 3 && sheet.id == ActionSheet.ACTIVE_ID) {
            //CoinFlipBottomSheetFragment.show(supportFragmentManager)
            val dialog = AttackDialogFragment.show(supportFragmentManager, playmat.board!!.player)
            dialog.attackListener = object : AttackDialogFragment.AttackListener {
                override fun onAttack(actions: List<Action>) {

                }
            }
        }
    }

    override fun onMenuItemSwitchChanged(sheet: ActionSheet, item: MenuItem, isChecked: Boolean) {
        toast("Menu Switch Changed (${item.id}, isChecked=$isChecked)")
    }

    override fun onMenuItemSpinnerSelected(sheet: ActionSheet, item: MenuItem, option: String, position: Int) {
        toast("Menu Spinner Item Selected(${item.id}, option=$option, position=$position)")
    }

    override fun onCountAccepted(count: Int) {
        toast("Count accepted: $count")
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, PlaytestActivity::class.java)
    }
}
