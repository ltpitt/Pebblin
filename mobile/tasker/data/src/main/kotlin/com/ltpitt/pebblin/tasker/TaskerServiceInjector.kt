package com.ltpitt.pebblin.tasker

interface TaskerServiceInjector {
   fun inject(taskerActionService: TaskerActionService)
   fun inject(legacyTaskerReceiver: LegacyTaskerReceiver)
}
