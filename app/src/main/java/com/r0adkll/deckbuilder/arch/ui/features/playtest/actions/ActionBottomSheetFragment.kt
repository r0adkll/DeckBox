package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.FragmentManager
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.utils.bindParcelable
import com.ftinc.kit.kotlin.utils.bundle
import com.ftinc.kit.kotlin.utils.spannable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.r0adkll.deckbuilder.R
import kotlinx.android.synthetic.main.fragment_playtest_actionsheet.*


class ActionBottomSheetFragment : BottomSheetDialogFragment() {

    interface MenuItemListener {

        fun onMenuItemActionClicked(item: MenuItem)
        fun onMenuItemSwitchChanged(item: MenuItem, isChecked: Boolean)
        fun onMenuItemSpinnerSelected(item: MenuItem, option: String, position: Int)
    }

    private val sheet by bindParcelable<ActionSheet>(KEY_ACTION_SHEET)
    var menuItemListener: MenuItemListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playtest_actionsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subheader.text = spannable {
            span(getString(R.string.playtest_title), ForegroundColorSpan(requireContext().color(R.color.black54)))
            span(getString(sheet.title), ForegroundColorSpan(requireContext().color(sheet.titleTint)))
        }.build()

        // Inflate menu items into container
        sheet.menuItems.forEach { menuItem ->
            when(menuItem) {
                is MenuItem.Action -> {
                    val view = layoutInflater.inflate(R.layout.item_playtest_action, actionContainer, false) as TextView
                    view.tag = menuItem
                    view.setText(menuItem.textResourceId)
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(view, menuItem.iconResourceId, 0, 0, 0)
                    TextViewCompat.setCompoundDrawableTintList(view, ColorStateList.valueOf(requireContext().color(R.color.black54)))
                    view.setOnClickListener {
                        menuItemListener?.onMenuItemActionClicked(it.tag as MenuItem)
                    }
                    actionContainer.addView(view)
                }
                is MenuItem.Switch -> {
                    val view = layoutInflater.inflate(R.layout.item_playtest_action_switch, actionContainer, false) as SwitchCompat
                    view.tag = menuItem
                    view.setText(menuItem.textResourceId)
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(view, menuItem.iconResourceId, 0, 0, 0)
                    TextViewCompat.setCompoundDrawableTintList(view, ColorStateList.valueOf(requireContext().color(R.color.black54)))
                    view.isChecked = menuItem.isChecked
                    view.setOnCheckedChangeListener { v, isChecked ->
                        menuItemListener?.onMenuItemSwitchChanged(v.tag as MenuItem, isChecked)
                    }
                    actionContainer.addView(view)
                }
                is MenuItem.Spinner -> {
                    val view = layoutInflater.inflate(R.layout.item_playtest_action_spinner, actionContainer, false)
                    val icon = view.findViewById<ImageView>(R.id.icon)
                    val spinner = view.findViewById<Spinner>(R.id.spinner)
                    icon.setImageResource(menuItem.iconResourceId)

                    val adapter = ArrayAdapter(requireContext(), R.layout.item_playtest_action, resources.getStringArray(menuItem.entriesResourceId))
                    adapter.setDropDownViewResource(R.layout.item_playtest_action)
                    spinner.adapter = adapter
                    spinner.setSelection(menuItem.currentEntry)
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val item = adapter.getItem(position)
                            menuItemListener?.onMenuItemSpinnerSelected(menuItem, item ?: "", position)
                        }
                    }

                    actionContainer.addView(view)
                }
                is MenuItem.Divider -> {
                    val view = layoutInflater.inflate(R.layout.item_playtest_action_divider, actionContainer, false)
                    actionContainer.addView(view)
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MenuItemListener) {
            menuItemListener = context
        }
    }


    companion object {
        const val TAG = "ActionBottomSheetFragment"
        private const val KEY_ACTION_SHEET = "ActionBottomSheetFragment.ActionSheet"

        /**
         * Show the action sheet with the passed configuration
         */
        fun show(fragmentManager: FragmentManager, actionSheet: ActionSheet): ActionBottomSheetFragment {
            val fragment = ActionBottomSheetFragment()
            fragment.arguments = bundle {
                KEY_ACTION_SHEET to actionSheet
            }
            fragment.show(fragmentManager, TAG)
            return fragment
        }

        fun FragmentManager.dismissActionSheet() {
            (this.findFragmentByTag(TAG) as? ActionBottomSheetFragment)
                    ?.dismiss()
        }
    }
}
