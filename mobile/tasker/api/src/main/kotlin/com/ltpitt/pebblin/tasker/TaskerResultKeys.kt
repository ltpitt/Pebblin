package com.ltpitt.pebblin.tasker

/**
 * Names of the local variables Pebblin returns to Tasker after an interactive action.
 *
 * These are the single source of truth for both the runtime that fills the result bundle
 * and the configuration screens that declare the variables back to Tasker.
 */
object TaskerResultKeys {
   const val STATUS = "%pebblin_status"
   const val RESULT_ID = "%pebblin_result_id"
   const val RESULT_VALUE = "%pebblin_result_value"
}
