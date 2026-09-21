package com.ltpitt.pebblin.tasker.ui.screens.actionlist

import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.test.FakePebblinActionRepository
import com.ltpitt.pebblin.actionlist.test.FakeDirectoryListRepository
import com.ltpitt.pebblin.navigation.keys.ActionListToggleKey
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import si.inova.kotlinova.core.outcome.Outcome
import si.inova.kotlinova.core.test.outcomes.shouldBeSuccessWithData
import si.inova.kotlinova.core.test.outcomes.testCoroutineResourceManager

class ActionListToggleViewModelTest {
   private val scope = TestScope()
   private val directoryRepo = FakeDirectoryListRepository()
   private val actionsRepo = FakePebblinActionRepository()

   private val vm = ActionListToggleViewModel(scope.testCoroutineResourceManager(), directoryRepo, actionsRepo, {})

   @Test
   fun `Load directory items`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListToggleKey(1)
      vm.onServiceRegistered()
      runCurrent()

      vm.uiState.value shouldBeSuccessWithData ActionListToggleState(
         PebblinDirectory(1, "Directory"),
         listOf(
            PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1),
            PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2),
         ),
         false
      )
   }

   @Test
   fun `Warn when there are more than 13 actions`() = scope.runTest {
      initDirectories()
      repeat(14) {
         actionsRepo.insert(PebblinAction("Action $it", 1, taskerTaskName = "Task A", id = it))
      }

      vm.key = ActionListToggleKey(1)
      vm.onServiceRegistered()
      runCurrent()

      vm.uiState.value.shouldBeInstanceOf<Outcome.Success<ActionListToggleState>>()
         .data
         .showActionsWarning shouldBe true
   }

   private suspend fun initDirectories() {
      directoryRepo.insert(PebblinDirectory(1, "Directory"))
      directoryRepo.insert(PebblinDirectory(2, "Another Directory"))
   }
}
