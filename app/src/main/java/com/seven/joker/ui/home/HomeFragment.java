package com.seven.joker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.seven.annotation.FragmentDestination;
import com.seven.joker.base.MutableDataSource;
import com.seven.joker.model.Feed;
import com.seven.joker.base.BaseFragment;

import java.util.List;

@FragmentDestination(pageUrl = "main/tabs/home", isStart = true, isLogin = false)
public class HomeFragment extends BaseFragment<Feed, HomeViewModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.getCacheLiveData().observe(this, new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                adapter.submitList(feeds);
            }
        });
    }
    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(), feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Feed feed = adapter.getCurrentList().get(adapter.getItemCount() - 1);
        viewModel.loadAfter(feed.id, new ItemKeyedDataSource.LoadCallback<Feed>() {
            @Override
            public void onResult(@NonNull List<Feed> data) {
                Log.e("qihao", "onLoadMore 6666"+data.toString());
                PagedList.Config config = adapter.getCurrentList().getConfig();
                if (data != null && data.size() > 0) {
                    MutableDataSource dataSource = new MutableDataSource();
                    dataSource.data.addAll(data);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    submitList(pagedList);
                }
            }
        });


    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        viewModel.getDataSource().invalidate();
    }
}