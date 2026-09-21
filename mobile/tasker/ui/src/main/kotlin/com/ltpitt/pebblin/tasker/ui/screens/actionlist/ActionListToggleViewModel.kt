package com.ltpitt.pebblin.tasker.ui.screens.actionlist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinActionRepository
import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.api.DirectoryListRepository
import com.ltpitt.pebblin.actionlist.api.MAX_ACTIONS_TO_SYNC
import com.ltpitt.pebblin.common.logging.ActionLogger
import com.ltpitt.pebblin.navigation.keys.ActionListToggleKey
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import si.inova.kotlinova.core.outcome.CoroutineResourceManager
import si.inova.kotlinova.core.outcome.Outcome
import si.inova.kotlinova.core.outcome.flatMapLatestOutcome
import si.inova.kotlinova.core.outcome.mapData
import si.inova.kotlinova.navigation.services.ContributesScopedService
import si.inova.kotlinova.navigation.services.SingleScreenViewModel

@Stable
@Inject
@ContributesScopedService
class ActionListToggleViewModel(
   private val resources: CoroutineResourceManager,
   private val directoryRepo: DirectoryListRepository,
   private val actionsRepo: PebblinActionRepository,
   private val actionLogger: ActionLogger,
) : SingleScreenViewModel<ActionListToggleKey>(resources.scope) {
   private val _uiState = MutableStateFlow<Outcome<ActionListToggleState>>(Outcome.Progress())
   val uiState: StateFlow<Outcome<ActionListToggleState>> = _uiState

   override fun onServiceRegistered() {
      actionLogger.logAction { "ActionListToggleViewModel.onServiceRegistered(id = $key)" }

      resources.launchResourceControlTask(_uiState) {
         val flow = directoryRepo.getSingle(key.directoryId).flatMapLatestOutcome { directory ->
            actionsRepo.getAll(directory.id).map { outcome ->
               outcome.mapData { list ->
                  ActionListToggleState(directory, list, list.size > MAX_ACTIONS_TO_SYNC)
               }
            }
         }

         emitAll(flow)
      }
   }
}

@Immutable
data class ActionListToggleState(
   val directory: PebblinDirectory,
   val actions: List<PebblinAction>,
   val showActionsWarning: Boolean,
)
