package com.ltpitt.pebblin.bluetooth

interface WatchappOpenController {
   fun isNextWatchappOpenForAutoSync(): Boolean
   fun setNextWatchappOpenForAutoSync()
   fun resetNextWatchappOpen()
}
