package com.guru.ricknmorty.di

import com.guru.ricknmorty.network.ApiManager
import com.guru.ricknmorty.network.IApiManager
import dagger.Binds
import dagger.Module

@Module
abstract class ManagersModule {

    @Binds
    abstract fun bindsApiManager(apiManager: ApiManager): IApiManager
}