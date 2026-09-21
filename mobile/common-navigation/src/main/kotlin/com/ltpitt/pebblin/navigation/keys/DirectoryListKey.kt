package com.ltpitt.pebblin.navigation.keys

import com.ltpitt.pebblin.navigation.keys.base.BaseScreenKey
import com.ltpitt.pebblin.navigation.keys.base.ListKey
import kotlinx.serialization.Serializable

@Serializable
data class DirectoryListKey(
   val targetScreen: String = ActionListKey::class.java.name,
) : BaseScreenKey(), ListKey
