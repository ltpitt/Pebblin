package com.ltpitt.pebblin.bluetooth

interface WatchSyncer {
   suspend fun init()

   suspend fun syncDirectory(id: Int)
   suspend fun deleteDirectory(id: Int)
}
