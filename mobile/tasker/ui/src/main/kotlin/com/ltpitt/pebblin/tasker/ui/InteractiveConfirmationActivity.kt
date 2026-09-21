package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.interactive.InteractiveConfirmationScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class InteractiveConfirmationActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> = listOf(InteractiveConfirmationScreenKey)
}
