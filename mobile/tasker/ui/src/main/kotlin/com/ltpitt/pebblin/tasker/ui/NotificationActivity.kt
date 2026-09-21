package com.ltpitt.pebblin.tasker.ui

import com.ltpitt.pebblin.tasker.ui.screens.notification.NotificationScreenKey
import si.inova.kotlinova.navigation.screenkeys.ScreenKey

class NotificationActivity : TaskerConfigurationActivity() {
   override fun getInitialHistory(): List<ScreenKey> = listOf(NotificationScreenKey)
}
