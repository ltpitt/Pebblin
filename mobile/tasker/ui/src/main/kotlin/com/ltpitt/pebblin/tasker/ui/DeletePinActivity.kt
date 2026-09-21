package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.createpin.DeletePinScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class DeletePinActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> {
      return listOf(DeletePinScreenKey)
   }
}
