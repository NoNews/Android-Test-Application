package ru.alexbykov.revoluttest.common.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseMvpPresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    protected fun disposeOnDetach(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun attachView(view: V) {
        super.attachView(view)
        onAttach()
    }

    open fun onAttach() {

    }

    override fun detachView(view: V) {
        super.detachView(view)
        onDetach()
    }

    open fun onDetach() {
        compositeDisposable.dispose()
    }


}
