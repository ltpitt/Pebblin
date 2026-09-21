package com.ltpitt.pebblin.actionlist.data

import com.ltpitt.pebblin.actionlist.api.PebblinAction
import com.ltpitt.pebblin.actionlist.api.PebblinDirectory
import com.ltpitt.pebblin.actionlist.sqldelight.generated.DbDirectory
import com.ltpitt.pebblin.actionlist.sqldelight.generated.SelectAll
import com.ltpitt.pebblin.actionlist.sqldelight.generated.SelectSingle

internal fun DbDirectory.toDirectory(): PebblinDirectory {
   return PebblinDirectory(id.toInt(), title)
}

internal fun PebblinDirectory.toDb(): DbDirectory {
   return DbDirectory(id.toLong(), title)
}

internal fun SelectAll.toPebblinAction(): PebblinAction {
   return PebblinAction(
      title = title,
      directoryId = directoryId.toInt(),
      id = id.toInt(),
      taskerTaskName = taskerTaskName,
      targetDirectoryId = targetDirectoryId?.toInt(),
      targetDirectoryName = targetDirectoryName,
      enabled = enabled == 1L,
      voiceArgument = voiceArgument == 1L
   )
}

internal fun SelectSingle.toPebblinAction(): PebblinAction {
   return PebblinAction(
      title = title,
      directoryId = directoryId.toInt(),
      id = id.toInt(),
      taskerTaskName = taskerTaskName,
      targetDirectoryId = targetDirectoryId?.toInt(),
      targetDirectoryName = targetDirectoryName,
      enabled = enabled == 1L
   )
}
