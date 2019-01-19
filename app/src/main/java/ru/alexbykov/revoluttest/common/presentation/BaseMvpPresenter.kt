package ru.alexbykov.revoluttest.common.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseMvpPresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    protected fun disposeOnPause(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun detachView(view: V) {
        compositeDisposable.dispose()
        super.detachView(view)
    }

}
