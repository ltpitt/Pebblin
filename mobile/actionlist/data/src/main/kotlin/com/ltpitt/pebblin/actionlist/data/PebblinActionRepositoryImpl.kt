package com.ltpitt.pebblin.actionlist.data

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinActionRepository
import com.ltpitt.pebblin.actionlist.sqldelight.generated.DbActionQueries
import com.ltpitt.pebblin.actionlist.sqldelight.generated.SelectAll
import com.ltpitt.pebblin.actionlist.sqldelight.generated.SelectSingle
import com.ltpitt.pebblin.bluetooth.WatchSyncer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dispatch.core.withDefault
import dispatch.core.withIO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import si.inova.kotlinova.core.exceptions.UnknownCauseException
import si.inova.kotlinova.core.outcome.Outcome

@Inject
@ContributesBinding(AppScope::class)
class PebblinActionRepositoryImpl(
   private val dbActionQueries: DbActionQueries,
   private val watchSyncer: WatchSyncer,
) : PebblinActionRepository {
   override fun getAll(directory: Int, limit: Int, onlyEnabled: Boolean): Flow<Outcome<List<PebblinAction>>> {
      val enabledNot = if (onlyEnabled) 0L else 2L
      return dbActionQueries.selectAll(directory.toLong(), enabledNot, limit.toLong()).asFlow()
         .map<Query<SelectAll>, Outcome<List<PebblinAction>>> { query ->
            withDefault {
               val list = query.awaitAsList()

               Outcome.Success(list.map { it.toPebblinAction() })
            }
         }
         .catch { emit(Outcome.Error(UnknownCauseException(cause = it))) }
   }

   override fun getById(id: Int): Flow<Outcome<PebblinAction?>> {
      return dbActionQueries.selectSingle(id.toLong()).asFlow()
         .map<Query<SelectSingle>, Outcome<PebblinAction?>> { query ->
            withDefault {
               val entry = query.awaitAsOneOrNull()

               Outcome.Success(entry?.toPebblinAction())
            }
         }
         .catch { emit(Outcome.Error(UnknownCauseException(cause = it))) }
   }

   override suspend fun insert(action: PebblinAction) = withIO<Unit> {
      dbActionQueries.insert(
         title = action.title,
         directoryId = action.directoryId.toLong(),
         taskerTaskName = action.taskerTaskName,
         targetDirectoryId = action.targetDirectoryId?.toLong(),
         enabled = if (action.enabled) 1L else 0L,
         voiceArgument = if (action.voiceArgument) 1L else 0L
      )

      watchSyncer.syncDirectory(action.directoryId)
   }

   override suspend fun update(id: Int, title: String, enabled: Boolean, voiceArgument: Boolean) {
      withIO {
         val directoryId = dbActionQueries.getDirectoryId(id.toLong()).executeAsOne().toInt()
         dbActionQueries.update(
            title = title,
            enabled = if (enabled) 1L else 0L,
            id = id.toLong(),
            voiceArgument = if (voiceArgument) 1L else 0L
         )

         watchSyncer.syncDirectory(directoryId)
      }
   }

   override suspend fun delete(id: Int) {
      withIO {
         val directoryId = dbActionQueries.getDirectoryId(id.toLong()).executeAsOne().toInt()
         dbActionQueries.delete(id.toLong())

         watchSyncer.syncDirectory(directoryId)
      }
   }

   override suspend fun reorder(id: Int, toIndex: Int) {
      withIO {
         val currentAction = dbActionQueries.selectSingleRaw(id.toLong()).executeAsOne()
         val fromIndex = currentAction.sortOrder
         val directoryId = currentAction.directoryId

         if (toIndex > fromIndex) {
            dbActionQueries.reorderUpwards(
               id = id.toLong(),
               fromIndex = fromIndex,
               toIndex = toIndex.toLong(),
               directoryId = directoryId
            )
         } else {
            dbActionQueries.reorderDownwards(
               id = id.toLong(),
               fromIndex = fromIndex,
               toIndex = toIndex.toLong(),
               directoryId = directoryId
            )
         }

         watchSyncer.syncDirectory(directoryId.toInt())
      }
   }

   override suspend fun massToggle(directory: Int, enable: List<Int>, disable: List<Int>) {
      withIO {
         dbActionQueries.toggle(enable = enable.map { it.toLong() }, disable = disable.map { it.toLong() })
         watchSyncer.syncDirectory(directory)
      }
   }
}
