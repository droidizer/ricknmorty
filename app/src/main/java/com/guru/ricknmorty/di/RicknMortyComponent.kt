package com.blackbelt.githubcodechallenge.di

import com.guru.ricknmorty.RickAndMortyApp
import com.guru.ricknmorty.di.ManagersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        BuildersModule::class, ManagersModule::class,
        NetworkModule::class, HelpersModule::class))
interface RicknMortyComponent {

    fun inject(app: RickAndMortyApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: RickAndMortyApp): Builder

        fun build(): RicknMortyComponent
    }
}