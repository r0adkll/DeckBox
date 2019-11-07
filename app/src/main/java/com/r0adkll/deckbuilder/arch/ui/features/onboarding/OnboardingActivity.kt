package com.r0adkll.deckbuilder.arch.ui.features.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.util.bindParcelable
import com.ftinc.kit.util.bundle
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding_page.*
import javax.inject.Inject

class OnboardingActivity : BaseActivity() {

    @Inject lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        pager.adapter = OnboardingPagerAdapter(PAGES, supportFragmentManager)
        pager.addOnPageChangeListener(IndicatorPageChangeListener())
        indicator.setupWithViewPager(pager)

        action_skip.setOnClickListener { launchSetup() }
        action_finish.setOnClickListener { launchSetup() }
        action_next.setOnClickListener {
            if (pager.currentItem < PAGES.size - 1) {
                pager.setCurrentItem(pager.currentItem + 1, true)
            } else {
                launchSetup()
            }
        }

        Analytics.event(Event.TutorialBegin)
    }

    override fun setupComponent() {
        DeckApp.component.inject(this)
    }

    private fun launchSetup() {
        Analytics.event(Event.TutorialComplete)
        startActivity(SetupActivity.createIntent(this))
        finish()
        preferences.onboarding = true
    }

    inner class IndicatorPageChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            when (position) {
                1 -> {
                    action_next.alpha = 1f - positionOffset
                    action_finish.alpha = positionOffset
                }
            }
        }

        override fun onPageSelected(position: Int) {
            when (position) {
                1 -> {
                    action_finish.alpha = 0f
                    action_finish.isGone = true
                    action_next.alpha = 1f
                    action_next.isVisible = true
                }
                2 -> {
                    action_finish.alpha = 1f
                    action_finish.isVisible = true
                    action_next.alpha = 0f
                    action_next.isGone = true
                }
            }
        }
    }

    class OnboardingPagerAdapter(
        private val pages: List<Page>,
        fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            val page = pages[position]
            return PageFragment.newInstance(page)
        }

        override fun getCount(): Int = pages.size
    }

    @Parcelize
    data class Page(
        @StringRes val title: Int,
        @StringRes val subtitle: Int,
        @DrawableRes val image: Int,
        val comingSoon: Boolean = false
    ) : Parcelable

    class PageFragment : Fragment() {

        val page by bindParcelable<Page>(KEY_PAGE)

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_onboarding_page, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            title.setText(page.title)
            subtitle.setText(page.subtitle)
            image.setImageResource(page.image)
            comingSoon.isVisible = page.comingSoon
        }

        companion object {
            const val KEY_PAGE = "PageFragment.Page"

            fun newInstance(page: Page): PageFragment {
                val fragment = PageFragment()
                fragment.arguments = bundle { KEY_PAGE to page }
                return fragment
            }
        }
    }

    companion object {
        val PAGES = listOf(
            Page(
                R.string.onboarding_title_search,
                R.string.onboarding_subtitle_search,
                R.drawable.dr_onboarding_symbol_1
            ),
            Page(
                R.string.onboarding_title_build,
                R.string.onboarding_subtitle_build,
                R.drawable.dr_onboarding_symbol_2
            ),
            Page(
                R.string.onboarding_title_share,
                R.string.onboarding_subtitle_share,
                R.drawable.dr_onboarding_symbol_3,
                true
            )
        )

        fun createIntent(context: Context): Intent = Intent(context, OnboardingActivity::class.java)
    }
}
