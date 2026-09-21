package com.ltpitt.pebblin.actionlist.ui.task

import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.test.FakePebblinActionRepository
import com.ltpitt.pebblin.actionlist.test.FakeDirectoryListRepository
import com.ltpitt.pebblin.navigation.keys.ActionListKey
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import si.inova.kotlinova.core.outcome.Outcome
import si.inova.kotlinova.core.test.outcomes.shouldBeSuccessWithData
import si.inova.kotlinova.core.test.outcomes.testCoroutineResourceManager

class ActionListViewModelTest {
   private val scope = TestScope()
   private val directoryRepo = FakeDirectoryListRepository()
   private val actionsRepo = FakePebblinActionRepository()

   private val vm = ActionListViewModel(scope.testCoroutineResourceManager(), directoryRepo, actionsRepo, {})

   @Test
   fun `Load directory items`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      runCurrent()

      vm.uiState.value shouldBeSuccessWithData ActionListState(
         PebblinDirectory(1, "Directory"),
         listOf(
            PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1),
            PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2),
         ),
         false
      )
   }

   @Test
   fun `Allow adding new Tasker task`() = scope.runTest {
      initDirectories()

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      vm.add("A Task", "My Task", null, false)
      vm.add("Another Task", "My Task", null, true)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("A Task", directoryId = 1, id = 1, taskerTaskName = "My Task"),
         PebblinAction("Another Task", directoryId = 1, id = 2, taskerTaskName = "My Task", voiceArgument = true)
      )
   }

   @Test
   fun `Allow adding new Directory link`() = scope.runTest {
      initDirectories()

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      vm.add("A Task", null, 2, false)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("A Task", directoryId = 1, id = 1, targetDirectoryId = 2)
      )
   }

   @Test
   fun `Allow editing name and voice argument of the existing action`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      runCurrent()
      vm.editActionTitleVoiceArgument(2, "Action C", true)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1),
         PebblinAction("Action C", 1, taskerTaskName = "Task B", id = 2, voiceArgument = true)
      )
   }

   @Test
   fun `Allow editing the enabled status of the existing action`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      runCurrent()
      vm.editActionEnabled(2, false)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1),
         PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2, enabled = false)
      )
   }

   @Test
   fun `Allow deleting the existing action`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      vm.deleteAction(1)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2)
      )
   }

   @Test
   fun `Allow reordering name of the existing actions`() = scope.runTest {
      initDirectories()
      actionsRepo.insert(PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1))
      actionsRepo.insert(PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2))

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      vm.reorder(1, 1)
      runCurrent()

      actionsRepo.getAll(1).first() shouldBeSuccessWithData listOf(
         PebblinAction("Action B", 1, taskerTaskName = "Task B", id = 2),
         PebblinAction("Action A", 1, taskerTaskName = "Task A", id = 1),
      )
   }

   @Test
   fun `Warn when there are more than 13 actions`() = scope.runTest {
      initDirectories()
      repeat(14) {
         actionsRepo.insert(PebblinAction("Action $it", 1, taskerTaskName = "Task A", id = it))
      }

      vm.key = ActionListKey(1)
      vm.onServiceRegistered()
      runCurrent()

      vm.uiState.value.shouldBeInstanceOf<Outcome.Success<ActionListState>>()
         .data
         .showActionsWarning shouldBe true
   }

   private suspend fun initDirectories() {
      directoryRepo.insert(PebblinDirectory(1, "Directory"))
      directoryRepo.insert(PebblinDirectory(2, "Another Directory"))
   }
}
