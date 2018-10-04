package com.blackbelt.githubcodechallenge.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guru.ricknmorty.RickAndMortyApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelpersModule {

    @Provides
    fun provideContext(app : RickAndMortyApp) : Context = app.applicationContext

    @Provides
    fun provideResources(context: Context) = context.resources

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}