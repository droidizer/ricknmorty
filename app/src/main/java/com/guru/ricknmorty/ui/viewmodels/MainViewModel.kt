package com.guru.ricknmorty.ui.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.res.Resources
import android.databinding.Bindable
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageWrapper
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder
import com.blackbelt.bindings.recyclerviewbindings.ItemClickListener
import com.blackbelt.bindings.recyclerviewbindings.PageDescriptor
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.LoadingIndicatorViewModel
import com.blackbelt.githubcodechallenge.view.misc.viewmodel.ProgressLoader
import com.guru.ricknmorty.BR
import com.guru.ricknmorty.R
import com.guru.ricknmorty.network.ApiManager
import com.guru.ricknmorty.network.IApiManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel constructor(apiManager: IApiManager, resources: Resources) : LoadingIndicatorViewModel() {

    @Bindable
    fun getItemDecoration(): RecyclerView.ItemDecoration = mItemDecoration

    @Bindable
    fun getNextPage() = mPageDescriptor

    fun setNextPage(pageDescriptor: PageDescriptor) {
        mRepositoriesDisposable.dispose()
        handleLoading(true)
        mRepositoriesDisposable =
                mApiManager.searchCharacter(mCurrentSearchString, pageDescriptor.getCurrentPage())
                        .map {
                            it.map { mItems.add(CharacterItemViewModel(it)) }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            handleLoading(false)
                            notifyPropertyChanged(BR.characters)
                        }, this::handlerError)
    }

    override fun onCreate() {
        super.onCreate()
        subscribeForSearchQueryChanges()
    }

    private fun subscribeForSearchQueryChanges() {
        mSearchNotifierDisposable =
                mSearchStringPublishSubject
                        .map { searchString ->
                            isSearchFieldEmpty = searchString.isEmpty()
                             notifyPropertyChanged(BR.clearSearchVisible)
                            searchString
                        }
                        .debounce(1, TimeUnit.SECONDS)
                        .filter { s -> !s.isEmpty() }
                        .subscribe(this::search, Throwable::printStackTrace)
    }

    open fun search(repo: String) {
        mItems.clear()
        notifyPropertyChanged(BR.characters)
        mCurrentSearchString = repo
        mPageDescriptor.setCurrentPage(1)
        setNextPage(mPageDescriptor)
    }

    fun setSearchQueryStringChanged(text: String) {
        mSearchStringPublishSubject.onNext(text)
    }

    @Bindable
    fun isClearSearchVisible(): Boolean = !isSearchFieldEmpty

    fun clearSearch() {
        mCurrentSearchString = ""
        isSearchFieldEmpty = true
       notifyPropertyChanged(BR.searchQueryString)
    }

    @Bindable
    fun getSearchQueryString(): String = mCurrentSearchString

    fun getItemClickListener(): ItemClickListener {
        return object : ItemClickListener {
            override fun onItemClicked(view: View, item: Any) {
                val repositoryViewModel = item as? CharacterItemViewModel ?: return
                mItemClickNotifier.value = ClickItemWrapper.withAdditionalData(0, repositoryViewModel.getCharacter())
            }
        }
    }

    private val mResources = resources

    private val mTemplates: Map<Class<*>, AndroidItemBinder> =
            hashMapOf(ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
                    CharacterItemViewModel::class.java to AndroidItemBinder(R.layout.character_item, BR.characterItemViewModel))

    private var mFirstLoading: Boolean = false

    private val mApiManager = apiManager

    private var mRepositoriesDisposable = Disposables.disposed()

    private var mSearchNotifierDisposable = Disposables.disposed()

    private val mSearchStringPublishSubject: PublishSubject<String> = PublishSubject.create()

    private var isSearchFieldEmpty: Boolean = true

    private var mCurrentSearchString = ""

    private val mItemDecoration by lazy {
        val margin: Int = mResources.getDimension(R.dimen.margin_4).toInt()
        val lateralMargin: Int = mResources.getDimension(R.dimen.margin_16).toInt()
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect?.bottom = margin
                outRect?.top = margin
                outRect?.left = lateralMargin
                outRect?.right = lateralMargin
            }
        }
    }

    @Bindable
    fun getTemplatesForObjects(): Map<Class<*>, AndroidItemBinder> = mTemplates

    @Bindable
    fun getCharacters(): List<Any> = mItems

    fun getItemClickNotifier() = mItemClickNotifier

    override fun onCleared() {
        super.onCleared()
        mRepositoriesDisposable.dispose()
        mSearchNotifierDisposable.dispose()
    }

    private fun setFistLoading(loading: Boolean) {
        mFirstLoading = loading
         notifyPropertyChanged(BR.firstLoading)
    }

    private fun setLoading(loading: Boolean) {
        if (loading && !mItems.contains(mProgressLoader)) {
            mItems.add(mProgressLoader)
        } else if (!loading) {
            mItems.remove(mProgressLoader)
        }
       notifyPropertyChanged(BR.characters)
    }

    override fun handlerError(throwable: Throwable) {
        super.handlerError(throwable)
        handleLoading(false)
        mMessageNotifier.value = MessageWrapper.withSnackBar(throwable.message ?: return)
    }

    class Factory @Inject constructor(resources: Resources, apiManager: ApiManager) : ViewModelProvider.NewInstanceFactory() {

        private val resources = resources
        private val apiManager = apiManager

        override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(apiManager, resources) as T
    }
}