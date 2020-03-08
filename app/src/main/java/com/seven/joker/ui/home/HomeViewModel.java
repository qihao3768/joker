package com.seven.joker.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.seven.joker.model.Feed;
import com.seven.joker.network.ApiResponse;
import com.seven.joker.network.ApiService;
import com.seven.joker.network.JsonCallback;
import com.seven.joker.network.Request;
import com.seven.joker.view.AbsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeViewModel extends AbsViewModel<Feed> {
    private volatile boolean withCache = true;

    @Override
    public DataSource createDataSource() {
        return dataSource;
    }

    ItemKeyedDataSource<Integer, Feed> dataSource = new ItemKeyedDataSource<Integer, Feed>() {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            loadData(0, callback);
            withCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    };

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType", null)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    Log.e("onsuccess" ,"qihao   "+response.body.size());
                }
            });
        }

        try {
            Request netRequest = withCache ? request.clone() : request;
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);

            if (key > 0) {
                getBoundaryPageData().postValue(data.size() > 0);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

}