package com.ltpitt.pebblin.tasker

interface TaskerTaskStarter {
   fun startTask(task: String, parameter: String?): Boolean
}
