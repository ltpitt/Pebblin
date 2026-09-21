package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.createpin.CreatePinScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class CreatePinActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> {
      return listOf(CreatePinScreenKey)
   }
}
