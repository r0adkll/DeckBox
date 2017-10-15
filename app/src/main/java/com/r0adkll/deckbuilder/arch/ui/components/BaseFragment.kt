package com.r0adkll.deckbuilder.arch.ui.components

import android.os.Bundle
import android.support.v4.app.Fragment
import gov.scstatehouse.houseofcards.di.HasComponent
import kotlin.reflect.KClass


abstract class BaseFragment : Fragment() {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupComponent()
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
