package com.guru.ricknmorty.network

import com.guru.ricknmorty.models.CharactersResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("character")
    fun getCharacters(@Query("page") page: Int): Observable<CharactersResponseModel>

    @GET("character")
    fun searchCharacters(@Query("page") page: Int, @Query("name") queryCharacter: String): Observable<CharactersResponseModel>
}