package com.ltpitt.pebblin.di

import android.app.Application
import com.matejdro.bucketsync.background.BackgroundSyncNotifier
import com.ltpitt.pebblin.MainViewModel
import com.ltpitt.pebblin.bluetooth.WatchSyncer
import com.ltpitt.pebblin.common.di.NavigationInjectingGraph
import com.ltpitt.pebblin.logging.FileLoggingController
import com.ltpitt.pebblin.logging.TinyLogLoggingThread
import com.ltpitt.pebblin.navigation.scenes.TabListDetailScene
import com.ltpitt.pebblin.receiving.PebbleListenerService
import com.ltpitt.pebblin.tasker.TaskerServiceInjector
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dispatch.core.DefaultCoroutineScope
import si.inova.kotlinova.core.reporting.ErrorReporter
import si.inova.kotlinova.core.time.AndroidDateTimeFormatter
import si.inova.kotlinova.navigation.conditions.ConditionalNavigationHandler
import si.inova.kotlinova.navigation.di.NavigationContext
import si.inova.kotlinova.navigation.di.NavigationInjection
import si.inova.kotlinova.navigation.di.OuterNavigationScope
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class, additionalScopes = [OuterNavigationScope::class])
interface MainApplicationGraph : ApplicationGraph {
   @DependencyGraph.Factory
   interface Factory {
      fun create(
         @Provides
         application: Application,
      ): MainApplicationGraph
   }

   @Multibinds(allowEmpty = true)
   fun provideEmptyConditionalMultibinds(): Map<KClass<*>, ConditionalNavigationHandler>
}

@Suppress("ComplexInterface") // DI
interface ApplicationGraph : NavigationInjectingGraph, TaskerServiceInjector {
   fun getErrorReporter(): ErrorReporter
   fun getDefaultCoroutineScope(): DefaultCoroutineScope
   override fun getNavigationInjectionFactory(): NavigationInjection.Factory
   override fun getNavigationContext(): NavigationContext
   fun getDateFormatter(): AndroidDateTimeFormatter
   fun getMainViewModelFactory(): MainViewModel.Factory
   fun getFileLoggingController(): FileLoggingController
   fun getTinyLogLoggingThread(): TinyLogLoggingThread
   fun getWatchSyncer(): WatchSyncer
   fun getTabListDetailSceneFactory(): TabListDetailScene.Factory
   fun getWorkerFactory(): PebblinWorkerFactory
   fun getSyncNotifier(): BackgroundSyncNotifier

   fun inject(target: PebbleListenerService)
}
