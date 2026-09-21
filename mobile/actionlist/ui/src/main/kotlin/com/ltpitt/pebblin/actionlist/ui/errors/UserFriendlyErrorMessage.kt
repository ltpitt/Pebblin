package com.ltpitt.pebblin.actionlist.ui.errors

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ltpitt.pebblin.actionlist.exception.MissingDirectoryException
import com.ltpitt.pebblin.ui.errors.commonUserFriendlyMessage
import si.inova.kotlinova.core.outcome.CauseException
import com.ltpitt.pebblin.sharedresources.R as sharedR

@Composable
fun CauseException.taskListUserFriendlyMessage(
   hasExistingData: Boolean = false,
): String {
   return if (this is MissingDirectoryException) {
      stringResource(sharedR.string.this_directory_does_not_exist_anymore)
   } else {
      commonUserFriendlyMessage(hasExistingData)
   }
}
