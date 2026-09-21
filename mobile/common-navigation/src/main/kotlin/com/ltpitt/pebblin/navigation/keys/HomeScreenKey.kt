package com.ltpitt.pebblin.navigation.keys

import com.ltpitt.pebblin.navigation.keys.base.BaseScreenKey
import com.ltpitt.pebblin.navigation.keys.base.TabContainerKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreenKey : BaseScreenKey(), TabContainerKey
