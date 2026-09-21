package com.ltpitt.pebblin.actionlist.api

import androidx.compose.runtime.Immutable

@Immutable
data class PebblinAction(
   public val title: String,
   public val directoryId: Int,
   public val id: Int = 0,
   public val taskerTaskName: String? = null,
   public val targetDirectoryId: Int? = null,
   public val targetDirectoryName: String? = null,
   public val voiceArgument: Boolean = false,
   public val enabled: Boolean = true,
)
