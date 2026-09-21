package com.ltpitt.pebblin.navigation.keys

import com.ltpitt.pebblin.navigation.keys.base.DetailKey
import kotlinx.serialization.Serializable
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

@Serializable
data class ActionListKey(val id: Int) : ScreenKey(), DetailKey
