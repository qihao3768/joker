package com.seven.joker.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class AbsViewModel<T> extends ViewModel {
    private DataSource dataSource;
    private LiveData<PagedList<T>> pageData;
    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();
    protected PagedList.Config config;

    public AbsViewModel() {
         config = new PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20)
                .build();
        DataSource.Factory factory = new DataSource.Factory() {
            @NonNull
            @Override
            public DataSource create() {
                dataSource = createDataSource();
                return dataSource;
            }
        };
        PagedList.BoundaryCallback callback = new PagedList.BoundaryCallback() {
            @Override
            public void onZeroItemsLoaded() {
                boundaryPageData.postValue(false);
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull Object itemAtFront) {
                boundaryPageData.postValue(true);
            }

            @Override
            public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
                super.onItemAtEndLoaded(itemAtEnd);
            }
        };
        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
                .setBoundaryCallback(callback)
                .build();
    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    public abstract DataSource createDataSource();


}
