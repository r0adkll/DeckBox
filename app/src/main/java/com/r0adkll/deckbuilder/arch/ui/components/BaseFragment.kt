package com.r0adkll.deckbuilder.arch.ui.components

import android.os.Bundle
import android.support.v4.app.Fragment
import com.r0adkll.deckbuilder.arch.ui.components.delegates.FragmentDelegate
import com.r0adkll.deckbuilder.arch.ui.components.delegates.StateSaverFragmentDelegate
import gov.scstatehouse.houseofcards.di.HasComponent
import kotlin.reflect.KClass


abstract class BaseFragment : Fragment() {

    private val delegates = ArrayList<FragmentDelegate>()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        delegates += StateSaverFragmentDelegate(this)
        setupComponent()

        delegates.forEach { it.onActivityCreated(savedInstanceState) }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        delegates.forEach { it.onSaveInstanceState(outState) }
        super.onSaveInstanceState(outState)
    }


    override fun onPause() {
        super.onPause()
        delegates.forEach(FragmentDelegate::onPause)
    }


    override fun onResume() {
        super.onResume()
        delegates.forEach(FragmentDelegate::onResume)
    }


    override fun onDestroy() {
        super.onDestroy()
        delegates.forEach(FragmentDelegate::onDestroy)
    }


    open fun setupComponent() {
    }


    protected fun <C : Any> getComponent(componentType: KClass<C>): C {
        if (activity is HasComponent<*>) {
            return componentType.java.cast((activity as HasComponent<*>).getComponent())!!
        }
        return componentType.java.cast((activity as HasComponent<*>).getComponent())!!
    }
}
