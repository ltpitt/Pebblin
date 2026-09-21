package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.syncnow.SyncNowScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class SyncNowActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> {
      return listOf(SyncNowScreenKey)
   }
}
