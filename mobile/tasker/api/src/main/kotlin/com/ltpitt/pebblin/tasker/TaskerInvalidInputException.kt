package com.ltpitt.pebblin.tasker

import si.inova.kotlinova.core.outcome.CauseException

class TaskerInvalidInputException(message: String) : CauseException(message, isProgrammersFault = false)
