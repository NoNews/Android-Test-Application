package ru.alexbykov.revoluttest

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class TrampolineSchedulerRule : TestRule {

    private val mScheduler = Schedulers.trampoline()


    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { scheduler -> mScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler -> mScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> mScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> mScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler -> mScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}

