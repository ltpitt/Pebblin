package com.ltpitt.pebblin.actionlist.test

import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.api.DirectoryListRepository
import com.ltpitt.pebblin.actionlist.exception.MissingDirectoryException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import si.inova.kotlinova.core.outcome.Outcome

class FakeDirectoryListRepository : DirectoryListRepository {
   var numCollections = 0

   private val direcories = MutableStateFlow<List<PebblinDirectory>>(emptyList())

   override fun getAll(): Flow<Outcome<List<PebblinDirectory>>> {
      return direcories.map { Outcome.Success(it) }.onStart { numCollections++ }
   }

   override fun getSingle(id: Int): Flow<Outcome<PebblinDirectory>> {
      return direcories.map { list ->
         val entry = list.firstOrNull { it.id == id }
         if (entry == null) {
            Outcome.Error(MissingDirectoryException())
         } else {
            Outcome.Success(entry)
         }
      }.onStart { numCollections++ }
   }

   override suspend fun insert(directory: PebblinDirectory) {
      direcories.update { it + directory }
   }

   override suspend fun update(directory: PebblinDirectory) {
      direcories.update { list -> list.filter { it.id != directory.id } + directory }
   }

   override suspend fun delete(id: Int) {
      direcories.update { list -> list.filter { it.id != id } }
   }
}
