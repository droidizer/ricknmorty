package com.blackbelt.githubcodechallenge.view.misc.viewmodel

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import com.blackbelt.bindings.recyclerviewbindings.PageDescriptor
import com.blackbelt.bindings.viewmodel.BaseViewModel
import com.guru.ricknmorty.BR

open class LoadingIndicatorViewModel : BaseViewModel() {

    private var mFirstLoading: Boolean = false

    private var mError = false

    protected var mPageDescriptor = PageDescriptor
            .setPageSize(24)
            .setStartPage(1)
            .setThreshold(5)
            .build()

    protected val mProgressLoader: ProgressLoader = ProgressLoader()

    protected val mItems = ObservableArrayList<Any>()

    protected fun handleLoading(loading: Boolean) {
        if (mPageDescriptor.getCurrentPage() == 1) {
            setFistLoading(loading)
        } else {
            setLoading(loading)
        }
    }

    override fun handlerError(throwable: Throwable) {
        super.handlerError(throwable)
    }

    private fun setFistLoading(loading: Boolean) {
        mFirstLoading = loading
        notifyPropertyChanged(BR.firstLoading)
    }

    private fun setLoading(loading: Boolean) {
        if (loading && !mItems.contains(mProgressLoader)) {
            mItems.add(mProgressLoader)
        } else if (!loading) {
            mItems.remove(mProgressLoader)
        }
       notifyPropertyChanged(BR.characters)
    }

    @Bindable
    fun isFirstLoading(): Boolean = mFirstLoading
}