package com.seven.joker.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.seven.joker.databinding.RefreshViewBinding;
import com.seven.joker.view.EmptyView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<T,M extends AbsViewModel<T>> extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    protected RefreshViewBinding binding;
    protected RecyclerView recyclerView;
    protected SmartRefreshLayout refreshLayout;
    protected EmptyView emptyView;
    protected PagedListAdapter<T, RecyclerView.ViewHolder> adapter;
    protected M viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RefreshViewBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;
        refreshLayout = binding.refreshView;
        emptyView = binding.emptyView;

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(null);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        genericViewModel();
        return binding.getRoot();
    }
    private void genericViewModel() {
        //利用 子类传递的 泛型参数实例化出absViewModel 对象。
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
            viewModel = (M) ViewModelProviders.of(this).get(modelClaz);

            //触发页面初始化数据加载的逻辑
            viewModel.getPageData().observe(this, pagedList -> submitList(pagedList));

            //监听分页时有无更多数据,以决定是否关闭上拉加载的动画
            viewModel.getBoundaryPageData().observe(this, hasData -> finishRefresh(hasData));
        }
    }


    public void submitList(PagedList<T> pagedList) {
        if (pagedList.size() > 0) {
            adapter.submitList(pagedList);
        }
        finishRefresh(pagedList.size() > 0);
    }

    public void finishRefresh(boolean hasData) {
        PagedList<T> currentList = adapter.getCurrentList();
        hasData = hasData || currentList != null && currentList.size() > 0;
        RefreshState state = refreshLayout.getState();
        if (state.isFooter && state.isOpening) {
            refreshLayout.finishLoadMore();
        } else if (state.isHeader && state.isOpening) {
            refreshLayout.finishRefresh();
        }
        if (hasData) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();

}
