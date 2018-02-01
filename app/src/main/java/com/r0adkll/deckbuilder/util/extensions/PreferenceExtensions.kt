package com.r0adkll.deckbuilder.util.extensions


import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import com.google.gson.internal.bind.util.ISO8601Utils
import com.google.gson.reflect.TypeToken
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.ptcgo.model.BasicEnergySet
import java.lang.reflect.Type
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


interface RxPreferences {

    val rxSharedPreferences: RxSharedPreferences


    abstract class ReactivePreference<T : Any?>(val key: String) : ReadOnlyProperty<RxPreferences, Preference<T>>


    class ReactiveIntPreference(key: String, val default: Int = 0) : ReactivePreference<Int>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<Int> {
            return thisRef.rxSharedPreferences.getInteger(key, default)
        }
    }


    class ReactiveBooleanPreference(key: String, val default: Boolean = false) : ReactivePreference<Boolean>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<Boolean> {
            return thisRef.rxSharedPreferences.getBoolean(key, default)
        }
    }


    class ReactiveStringPreference(key: String, val default: String? = null) : ReactivePreference<String>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<String> {
            return default?.let {
                return thisRef.rxSharedPreferences.getString(key, it)
            } ?: thisRef.rxSharedPreferences.getString(key)
        }
    }


    class ReactiveStringSetPreference(key: String, val default: Set<String> = HashSet()) : ReactivePreference<Set<String>>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<Set<String>> {
            return thisRef.rxSharedPreferences.getStringSet(key, default)
        }
    }


    class ReactiveExpansionsPreference(key: String): ReactivePreference<List<Expansion>>(key) {
        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<List<Expansion>> {
            return thisRef.rxSharedPreferences.getObject(key, listOf(), ExpansionConverter())
        }
    }


    class ReactiveBasicEnergySetPreference(key: String): ReactivePreference<BasicEnergySet>(key) {
        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<BasicEnergySet> {
            return thisRef.rxSharedPreferences.getObject(key, BasicEnergySet.SunMoon, BasicEnergySetConverter())
        }
    }


    class ReactiveDatePreference(key: String, val default: Date? = null) : ReactivePreference<Date>(key) {
        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<Date> {
            return thisRef.rxSharedPreferences.getObject(key, default ?: Date(), DateConverter())
        }
    }


    class ReactiveEnumPreference<T : Enum<T>>(key: String, val default: T) : ReactivePreference<T>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<T> {
            return thisRef.rxSharedPreferences.getEnum(key, default, default::class.java as Class<T>)
        }
    }


    class ReactiveJsonPreference<T : Any>(key: String, val default: T) : ReactivePreference<T>(key) {

        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<T> {
            return thisRef.rxSharedPreferences.getObject(key, default, GsonConverter<T>(default::class))
        }
    }


    private class DateConverter : Preference.Converter<Date> {

        override fun deserialize(serialized: String): Date {
            return ISO8601Utils.parse(serialized, ParsePosition(0))
        }

        override fun serialize(value: Date): String {
            return ISO8601Utils.format(value)
        }
    }


    private class BasicEnergySetConverter : Preference.Converter<BasicEnergySet> {

        override fun deserialize(serialized: String): BasicEnergySet {
            return BasicEnergySet::class.nestedClasses
                    .find { it.qualifiedName == serialized }?.objectInstance as? BasicEnergySet ?: BasicEnergySet.SunMoon
        }

        override fun serialize(value: BasicEnergySet): String {
            return value::class.qualifiedName!!
        }
    }


    private class ExpansionConverter : Preference.Converter<List<Expansion>> {

        private val gson = Gson()


        override fun deserialize(serialized: String): List<Expansion> {
            val type = object : TypeToken<List<@kotlin.jvm.JvmSuppressWildcards Expansion>>() {}.type
            return gson.fromJson(serialized, type)
        }


        override fun serialize(value: List<Expansion>): String {
            return gson.toJson(value)
        }
    }


    private class GsonConverter<T : Any>(
            val clazz: KClass<out T>
    ) : Preference.Converter<T> {

        private val gson = Gson()


        override fun deserialize(serialized: String): T {
            return gson.fromJson(serialized, clazz.java)
        }


        override fun serialize(value: T): String {
            return gson.toJson(value)
        }
    }

}