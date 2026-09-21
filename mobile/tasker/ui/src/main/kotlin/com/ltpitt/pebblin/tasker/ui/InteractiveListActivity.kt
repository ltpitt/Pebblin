package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.interactive.InteractiveListScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class InteractiveListActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> = listOf(InteractiveListScreenKey)
}
