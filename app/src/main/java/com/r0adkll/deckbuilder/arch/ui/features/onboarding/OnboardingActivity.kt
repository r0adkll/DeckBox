package com.r0adkll.deckbuilder.arch.ui.features.onboarding


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.gone
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.visible
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindOptionalParcelable
import com.r0adkll.deckbuilder.util.bundle
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding_page.*
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
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
            }
            else {
                launchSetup()
            }
        }
    }


    override fun setupComponent(component: AppComponent) {
        component.inject(this)
    }


    fun launchSetup() {
        startActivity(SetupActivity.createIntent(this))
        finish()
        preferences.onboarding = true
    }


    inner class IndicatorPageChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            when(position) {
                1 -> {
                    action_next.alpha = 1f - positionOffset
                    action_finish.alpha = positionOffset
                }
            }
        }

        override fun onPageSelected(position: Int) {
            when(position) {
                1 -> {
                    action_finish.alpha = 0f
                    action_finish.gone()
                    action_next.alpha = 1f
                    action_next.visible()
                }
                2 -> {
                    action_finish.alpha = 1f
                    action_finish.visible()
                    action_next.alpha = 0f
                    action_next.gone()
                }
            }
        }
    }


    class OnboardingPagerAdapter(
            val pages: List<Page>,
            fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            val page = pages[position]
            return PageFragment.newInstance(page)
        }


        override fun getCount(): Int = pages.size
    }


    @PaperParcel
    data class Page(
            @StringRes val title: Int,
            @StringRes val subtitle: Int,
            @DrawableRes val image: Int,
            val comingSoon: Boolean = false
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelOnboardingActivity_Page.CREATOR
        }
    }


    class PageFragment : Fragment() {

        val page: Page by bindOptionalParcelable(KEY_PAGE)


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_onboarding_page, container, false)
        }


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            title.setText(page.title)
            subtitle.setText(page.subtitle)
            image.setImageResource(page.image)
            comingSoon.setVisible(page.comingSoon)
        }


        companion object {
            const val KEY_PAGE = "PageFragment.Page"

            fun newInstance(page: Page): PageFragment {
                val fragment = PageFragment()
                fragment.arguments = bundle { parcel(KEY_PAGE, page) }
                return fragment
            }
        }
    }


    companion object {
        val PAGES = listOf(
                Page(R.string.onboarding_title_search, R.string.onboarding_subtitle_search, R.drawable.dr_onboarding_symbol_1),
                Page(R.string.onboarding_title_build, R.string.onboarding_subtitle_build, R.drawable.dr_onboarding_symbol_2),
                Page(R.string.onboarding_title_share, R.string.onboarding_subtitle_share, R.drawable.dr_onboarding_symbol_3, true)
        )

        fun createIntent(context: Context): Intent = Intent(context, OnboardingActivity::class.java)
    }
}