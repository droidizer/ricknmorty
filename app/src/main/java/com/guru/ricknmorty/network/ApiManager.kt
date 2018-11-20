package com.guru.ricknmorty.network

import com.guru.ricknmorty.models.Character
import com.guru.ricknmorty.models.CharactersResponseModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiManager @Inject constructor(apiService: ApiService) : IApiManager {

    private var mApiService = apiService

    private var mSearchDisposable = Disposables.disposed()

    private var mSubscribersDisposable = Disposables.disposed()

    override fun searchCharacter(query: String, page: Int): Observable<List<Character>> {
	        val characters: BehaviorSubject<List<Character>> = BehaviorSubject.create()
        mSearchDisposable.dispose()
        mSearchDisposable = mApiService.searchCharacters(page, query)
                .filter { model: CharactersResponseModel -> !model.items.isEmpty() }
                .subscribe({
                    characters.onNext(it.items)
                }, characters::onError, characters::onComplete)
        return characters.hide()
    }

    override fun getCharacters(page: Int): Observable<List<Character>> {
        val mCharactersSubject: BehaviorSubject<List<Character>> = BehaviorSubject.create()
        mSubscribersDisposable.dispose()
        mSubscribersDisposable = mApiService.getCharacters(page)
                .subscribe({ mCharactersSubject.onNext(it.items) }, mCharactersSubject::onError, mCharactersSubject::onComplete)

        return mCharactersSubject.hide()
    }
}


