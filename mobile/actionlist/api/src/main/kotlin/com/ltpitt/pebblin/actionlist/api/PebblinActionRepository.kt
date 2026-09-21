package com.ltpitt.pebblin.actionlist.api

import kotlinx.coroutines.flow.Flow
import si.inova.kotlinova.core.outcome.Outcome

interface PebblinActionRepository {
   fun getAll(directory: Int, limit: Int = Int.MAX_VALUE, onlyEnabled: Boolean = false): Flow<Outcome<List<PebblinAction>>>
   fun getById(id: Int): Flow<Outcome<PebblinAction?>>
   suspend fun insert(action: PebblinAction)
   suspend fun update(id: Int, title: String, enabled: Boolean, voiceArgument: Boolean)
   suspend fun delete(id: Int)
   suspend fun reorder(id: Int, toIndex: Int)
   suspend fun massToggle(directory: Int, enable: List<Int>, disable: List<Int>)
}
