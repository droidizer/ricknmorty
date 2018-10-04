package com.blackbelt.githubcodechallenge.di

import android.content.Context
import com.google.gson.Gson
import com.guru.ricknmorty.BuildConfig
import com.guru.ricknmorty.network.ApiService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECTION_TIMEOUT_SECS: Long = 20

private const val HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = (5 * 1024 * 1024).toLong()

@Module
class NetworkModule {

    @Provides
    fun provideCacheFile(context: Context): Cache {
        val baseDir = context.cacheDir
        val cacheDir = File(baseDir, "HttpResponseCache")
        return Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE)
    }

    @Provides
    @Singleton
    fun provideGsonConverterLibrary(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun providesRxJava2CallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache?): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (cache != null) {
            okHttpClientBuilder.cache(cache)
        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        okHttpClientBuilder
                .connectTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT_SECS, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGitHubApiServie(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): ApiService {
        val builder = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
        val retrofit = builder.client(okHttpClient).build()
        return retrofit.create(ApiService::class.java)
    }
}