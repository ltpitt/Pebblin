package com.ltpitt.pebblin.actionlist.ui.directory

import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.test.FakeDirectoryListRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import si.inova.kotlinova.core.test.outcomes.shouldBeSuccessWithData
import si.inova.kotlinova.core.test.outcomes.testCoroutineResourceManager

class DirectoryListViewModelTest {
   private val repo = FakeDirectoryListRepository()
   private val scope = TestScope()

   private val vm = DirectoryListViewModel(
      repo,
      scope.testCoroutineResourceManager(),
      {}
   )

   @Test
   fun `Pass directories to the UI State`() = scope.runTest {
      repo.insert(PebblinDirectory(1, "Hello Directory"))

      vm.onServiceRegistered()
      runCurrent()

      vm.uiState.value shouldBeSuccessWithData DirectoryListState(listOf(PebblinDirectory(1, "Hello Directory")))
   }

   @Test
   fun `Add should add a directory to the repo`() = scope.runTest {
      vm.add("New Directory")
      runCurrent()

      repo.getAll().first() shouldBeSuccessWithData listOf(PebblinDirectory(0, "New Directory"))
   }

   @Test
   fun `Edit should edit the existing directory`() = scope.runTest {
      repo.insert(PebblinDirectory(1, "Hello Directory"))

      vm.edit(1, "New Directory Name")
      runCurrent()

      repo.getAll().first() shouldBeSuccessWithData listOf(PebblinDirectory(1, "New Directory Name"))
   }

   @Test
   fun `Delete should delete the existing directory`() = scope.runTest {
      repo.insert(PebblinDirectory(1, "Hello Directory"))

      vm.delete(1)
      runCurrent()

      repo.getAll().first() shouldBeSuccessWithData emptyList()
   }
}
