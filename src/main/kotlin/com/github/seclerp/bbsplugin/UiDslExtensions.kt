package com.github.seclerp.bbsplugin

import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.ui.dsl.builder.MutableProperty

fun <T> ObservableMutableProperty<T>.toMutableProperty(): MutableProperty<T> {
    return MutableProperty({ get() }, { set(it) })
}