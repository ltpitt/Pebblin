package com.ltpitt.pebblin.actionlist.test

import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinActionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import si.inova.kotlinova.core.outcome.Outcome

class FakePebblinActionRepository : PebblinActionRepository {
   var numCollections = 0

   private val actions = MutableStateFlow<List<PebblinAction>>(emptyList())

   override fun getAll(directory: Int, limit: Int, onlyEnabled: Boolean): Flow<Outcome<List<PebblinAction>>> {
      return actions
         .map { list ->
            Outcome.Success(
               list.filter {
                  it.directoryId == directory && (!onlyEnabled || it.enabled)
               }.take(limit)
            )
         }
         .onStart { numCollections++ }
   }

   override fun getById(id: Int): Flow<Outcome<PebblinAction?>> {
      return actions
         .map { list -> Outcome.Success(list.firstOrNull { it.id == id }) }
         .onStart { numCollections++ }
   }

   override suspend fun insert(action: PebblinAction) {
      actions.update { it + action.copy(id = action.id.takeIf { it > 0 } ?: (it.size + 1)) }
   }

   fun insert(vararg action: PebblinAction) {
      actions.update { it + action }
   }

   override suspend fun update(id: Int, title: String, enabled: Boolean, voiceArgument: Boolean) {
      actions.update { list ->
         list.map {
            if (it.id == id) {
               it.copy(
                  title = title,
                  enabled = enabled,
                  voiceArgument = voiceArgument
               )
            } else {
               it
            }
         }
      }
   }

   override suspend fun reorder(id: Int, toIndex: Int) {
      actions.update { list ->
         val existing = list.first { it.id == id }
         list.toMutableList().apply {
            remove(existing)
            add(toIndex, existing)
         }
      }
   }

   override suspend fun delete(id: Int) {
      actions.update { list -> list.filter { it.id != id } }
   }

   override suspend fun massToggle(
      directory: Int,
      enable: List<Int>,
      disable: List<Int>,
   ) {
      actions.update { list ->
         list.map { action ->
            val enabled = if (enable.contains(action.id)) {
               true
            } else if (disable.contains(action.id)) {
               false
            } else {
               action.enabled
            }

            action.copy(enabled = enabled)
         }
      }
   }
}
