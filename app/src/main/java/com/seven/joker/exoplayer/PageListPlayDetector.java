package com.seven.joker.exoplayer;

import android.graphics.Point;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PageListPlayDetector {
    private List<IPlayTarget> targets = new ArrayList<>();
    private RecyclerView recyclerView;
    private IPlayTarget playingTarget;

    public void addTarget(IPlayTarget target) {
        targets.add(target);
    }

    public void removeTarget(IPlayTarget target) {
        targets.remove(target);
    }

    public PageListPlayDetector(LifecycleOwner owner, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    recyclerView.getAdapter().unregisterAdapterDataObserver(dataObserver);
                    owner.getLifecycle().removeObserver(this);
                }
            }
        });

        recyclerView.getAdapter().registerAdapterDataObserver(dataObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlay();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (playingTarget != null && playingTarget.isPlaying() && !isTargetInBounds(playingTarget)) {
                    playingTarget.inActive();
                }
            }
        });

    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            autoPlay();
        }
    };

    private void autoPlay() {
        if (targets.size() <= 0 || recyclerView.getChildCount() <= 0) {
            return;
        }
        if (playingTarget != null && playingTarget.isPlaying() && isTargetInBounds(playingTarget)) {
            return;
        }

        IPlayTarget activeTarget = null;
        for (IPlayTarget target : targets) {
            boolean inBounds = isTargetInBounds(target);
            if (inBounds) {
                activeTarget = target;
                break;
            }
        }

        if (activeTarget != null) {
            if (playingTarget != null && playingTarget.isPlaying()) {
                playingTarget.inActive();
            }
            playingTarget = activeTarget;
            playingTarget.onActive();
        }

    }

    private boolean isTargetInBounds(IPlayTarget target) {
        ensureRecyclerViewLocation();
        ViewGroup owner = target.getOwner();
        if (!owner.isShown() || !owner.isAttachedToWindow()) {
            return false;
        }

        int[] location = new int[2];
        owner.getLocationOnScreen(location);

        int center = location[1] + owner.getHeight() / 2;

        return center >= rvLocation.x && center <= rvLocation.y;
    }

    private Point rvLocation = null;

    private Point ensureRecyclerViewLocation() {
        if (rvLocation == null) {
            int[] location = new int[2];
            recyclerView.getLocationOnScreen(location);
            int top = location[1];
            int bottom = top + recyclerView.getHeight();
            rvLocation = new Point(top, bottom);
        }
        return rvLocation;
    }

    public void onPause() {
        if (playingTarget != null) {
            playingTarget.inActive();
        }
    }

    public void onResume() {
        if (playingTarget != null) {
            playingTarget.onActive();
        }
    }
}
