package com.guru.ricknmorty.viewmodels

import android.content.res.Resources
import com.blackbelt.github.uti.RxClassTestRule
import com.google.gson.Gson
import com.guru.ricknmorty.JsonFileReader
import com.guru.ricknmorty.models.Character
import com.guru.ricknmorty.models.CharactersResponseModel
import com.guru.ricknmorty.network.IApiManager
import com.guru.ricknmorty.ui.viewmodels.CharacterItemViewModel
import com.guru.ricknmorty.ui.viewmodels.MainViewModel
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val schedulers = RxClassTestRule()

    @Mock
    private lateinit var mApiManager: IApiManager

    @Mock
    private lateinit var mResources: Resources

    private lateinit var mMainViewModel: MainViewModel

    @Before
    fun setup() {
        mMainViewModel = Mockito.spy(MainViewModel(mApiManager, mResources))
        mMainViewModel.onCreate()
    }

    @Test
    fun `test_search_call`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        Mockito.`when`(mApiManager
                .searchCharacter("TEST",
                        pageDescriptor.getCurrentPage())).thenReturn(Observable.just(ArrayList()))

        mMainViewModel.setSearchQueryStringChanged("TEST")
        Mockito.verify(mMainViewModel).search("TEST")
    }

    @Test
    fun `test_search_empty_data_set`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        Mockito.`when`(mApiManager
                .searchCharacter("TEST",
                        pageDescriptor.getCurrentPage())).thenReturn(Observable.just(ArrayList()))

        mMainViewModel.setSearchQueryStringChanged("TEST")
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().isEmpty())
    }

    @Test
    fun `test_all_characters_data_set`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        Mockito.`when`(mApiManager
                .getCharacters(pageDescriptor.getCurrentPage()))
                .thenReturn(Observable.just(getAllCharacters()))

        mMainViewModel.loadCharacterList()
        org.junit.Assert.assertTrue(!mMainViewModel.getCharacters().isEmpty())
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().count() == 20)
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters()[0] is CharacterItemViewModel)
    }

    @Test
    fun `test_all_characters_empty_data`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        Mockito.`when`(mApiManager
                .getCharacters(pageDescriptor.getCurrentPage()))
                .thenReturn(Observable.just(ArrayList()))

        mMainViewModel.loadCharacterList()
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().isEmpty())
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().count() == 0)
    }

    @Test
    fun `test_search_valid_data_set`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        Mockito.`when`(mApiManager
                .searchCharacter("Rick",
                        pageDescriptor.getCurrentPage()))
                .thenReturn(Observable.just(getAllRickCharacters()))

        mMainViewModel.setSearchQueryStringChanged("Rick")
        org.junit.Assert.assertTrue(!mMainViewModel.getCharacters().isEmpty())
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().count() == 20)
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters()[0] is CharacterItemViewModel)
    }

    @Test
    fun `test_on_error`() {
        val pageDescriptor = mMainViewModel.getNextPage()
        val throwable = Throwable("HTTP 404 Not Found")

        Mockito.`when`(mApiManager
                .searchCharacter("TEST",
                        pageDescriptor.getCurrentPage())).thenReturn(Observable.error(throwable))

        mMainViewModel.setSearchQueryStringChanged("TEST")
        org.junit.Assert.assertTrue(mMainViewModel.getCharacters().isEmpty())
        Mockito.verify(mMainViewModel).handleError(mResources, throwable)
    }

    @Test
    fun `test_clear_search`() {
        mMainViewModel.clearSearch()
        org.junit.Assert.assertFalse(mMainViewModel.isClearSearchVisible())
        org.junit.Assert.assertTrue(mMainViewModel.getSearchQueryString().isNullOrEmpty())
    }

    private fun getAllRickCharacters(): List<Character> {
        val characterResponseModel = JsonFileReader.read(javaClass.classLoader.getResourceAsStream("rick.json"),
                Gson(), CharactersResponseModel::class.java).blockingFirst()
        return characterResponseModel.items
    }

    private fun getAllCharacters(): List<Character> {
        val characterResponseModel = JsonFileReader.read(javaClass.classLoader.getResourceAsStream("characters.json"),
                Gson(), CharactersResponseModel::class.java).blockingFirst()
        return characterResponseModel.items
    }
}