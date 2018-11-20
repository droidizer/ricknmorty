package com.blackbelt.github.uti

import android.support.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class RxClassTestRule : TestRule {

    private var mScheduler: Scheduler =
            object : Scheduler() {
                override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Scheduler.Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

    constructor()

    constructor(scheduler: Scheduler) {
        mScheduler = scheduler
    }

    fun updateComputationScheduler(newScheduler: Scheduler) {
        RxJavaPlugins.setComputationSchedulerHandler { newScheduler }
    }

    fun resetSchedulers() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { mScheduler }
                RxJavaPlugins.setInitComputationSchedulerHandler { mScheduler }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { mScheduler }
                RxJavaPlugins.setInitSingleSchedulerHandler { mScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { mScheduler }

                try {
                    base?.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}