package com.seven.joker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.seven.annotation.FragmentDestination;
import com.seven.joker.R;
import com.seven.joker.model.Feed;
import com.seven.joker.view.BaseFragment;

@FragmentDestination(pageUrl = "main/tabs/home",isStart = true,isLogin = false)
public class HomeFragment extends BaseFragment<Feed,HomeViewModel> {
    @Override
    protected void afterCreateView() {

    }

    @Override
    public PagedListAdapter getAdapter() {
        String feedType=getArguments()==null?"all":getArguments().getString("feedType");
        return new FeedAdapter(getContext(),feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}