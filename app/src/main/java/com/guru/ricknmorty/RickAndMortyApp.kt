package com.guru.ricknmorty

import android.app.Activity
import android.app.Application
import com.blackbelt.githubcodechallenge.di.DaggerRicknMortyComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class RickAndMortyApp: Application(), HasActivityInjector {

    @Inject
    lateinit var mAndroidDispatchingInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this);
        DaggerRicknMortyComponent.builder().application(this).build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mAndroidDispatchingInjector
}