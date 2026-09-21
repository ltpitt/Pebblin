package com.ltpitt.pebblin.actionlist.exception

import si.inova.kotlinova.core.outcome.CauseException

class MissingDirectoryException : CauseException(isProgrammersFault = false)
