package com.guru.ricknmorty.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import com.blackbelt.bindings.BR
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.BaseInjectableBindingActivity
import com.guru.ricknmorty.R
import com.guru.ricknmorty.models.Character
import com.guru.ricknmorty.network.IApiManager
import com.guru.ricknmorty.ui.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : BaseInjectableBindingActivity() {

    @Inject
    lateinit var mFactory: MainViewModel.Factory

    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                val view = currentFocus
                if (view != null) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0, null)
                }
            }
        }
    }

    private val mMainViewmodel by lazy {
        ViewModelProviders.of(this, mFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main, BR.mainViewModel, mMainViewmodel)
        findViewById<RecyclerView>(R.id.main_rv).addOnScrollListener(mOnScrollListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        findViewById<RecyclerView>(R.id.main_rv).removeOnScrollListener(mOnScrollListener)
    }
}
