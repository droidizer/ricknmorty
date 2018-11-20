package com.guru.ricknmorty.network

import com.guru.ricknmorty.models.Character
import io.reactivex.Observable


interface IApiManager {

    fun getCharacters(page: Int): Observable<List<Character>>

    fun searchCharacter(query: String, page: Int): Observable<List<Character>>

}