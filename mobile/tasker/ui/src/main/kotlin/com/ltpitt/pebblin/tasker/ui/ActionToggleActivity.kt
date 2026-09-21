package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.navigation.keys.ActionListToggleKey
import com.ltpitt.pebblin.navigation.keys.DirectoryListKey
import com.ltpitt.pebblin.tasker.BundleKeys
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class ActionToggleActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> {
      val existingDirectory = existingData.getInt(BundleKeys.DIRECTORY_ID, -1)
      return listOf(
         if (existingDirectory >= 0) {
            ActionListToggleKey(existingDirectory)
         } else {
            DirectoryListKey(ActionListToggleKey::class.java.name)
         }
      )
   }
}
