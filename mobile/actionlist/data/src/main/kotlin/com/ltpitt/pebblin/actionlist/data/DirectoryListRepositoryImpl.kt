package com.ltpitt.pebblin.actionlist.data

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.api.DirectoryListRepository
import com.ltpitt.pebblin.actionlist.exception.MissingDirectoryException
import com.ltpitt.pebblin.actionlist.sqldelight.generated.DbDirectoryQueries
import com.ltpitt.pebblin.bluetooth.WatchSyncer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dispatch.core.withDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import si.inova.kotlinova.core.outcome.Outcome

@Inject
@ContributesBinding(AppScope::class)
class DirectoryListRepositoryImpl(
   private val dbDirectoryQueries: DbDirectoryQueries,
   private val watchSyncer: WatchSyncer,
) : DirectoryListRepository {
   override fun getAll(): Flow<Outcome<List<PebblinDirectory>>> {
      return dbDirectoryQueries.selectAll().asFlow().map { query ->
         withDefault {
            val list = query.awaitAsList()

            if (list.isEmpty()) {
               insert(PebblinDirectory(0, "Starting Directory"))
            }

            Outcome.Success(list.map { it.toDirectory() })
         }
      }
   }

   override fun getSingle(id: Int): Flow<Outcome<PebblinDirectory>> {
      return dbDirectoryQueries.selectSingle(id.toLong()).asFlow().map { query ->
         withDefault {
            val value = query.awaitAsOneOrNull()

            if (value == null) {
               Outcome.Error(MissingDirectoryException())
            } else {
               Outcome.Success(value.toDirectory())
            }
         }
      }
   }

   override suspend fun insert(directory: PebblinDirectory) = withDefault<Unit> {
      dbDirectoryQueries.insert(directory.toDb())
      syncStartingDirectory()
   }

   override suspend fun update(directory: PebblinDirectory) {
      require(directory.id > 1) { "Starting directory cannot be updated" }

      withDefault {
         dbDirectoryQueries.update(directory.toDb())
         syncStartingDirectory()
      }
   }

   override suspend fun delete(id: Int) {
      require(id > 1) { "Starting directory cannot be deleted" }

      withDefault {
         dbDirectoryQueries.delete(id.toLong())
         watchSyncer.deleteDirectory(id)
         syncStartingDirectory()
      }
   }

   private suspend fun syncStartingDirectory() {
      // Generated folder entries live in the starting directory's bucket.
      watchSyncer.syncDirectory(STARTING_DIRECTORY_ID)
   }
}

private const val STARTING_DIRECTORY_ID = 1
