package com.ltpitt.pebblin.actionlist.api

import kotlinx.coroutines.flow.Flow
import si.inova.kotlinova.core.outcome.Outcome

interface DirectoryListRepository {
   fun getAll(): Flow<Outcome<List<PebblinDirectory>>>
   fun getSingle(id: Int): Flow<Outcome<PebblinDirectory>>
   suspend fun insert(directory: PebblinDirectory)
   suspend fun update(directory: PebblinDirectory)
   suspend fun delete(id: Int)
}
