package com.kotlin.base.rx

import com.kotlin.base.presenter.view.BaseView
import rx.Subscriber

/**
 * Rx订阅者默认实现
 */
open class BaseSubscriber<T>(private val BaseView:BaseView):Subscriber<T>() {

    override fun onCompleted() {
        BaseView.hideLoading()
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable?) {
        BaseView.hideLoading()
        if (e is BaseException){
            BaseView.onError(e.msg)
        }
    }
}
