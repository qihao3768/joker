package com.seven.joker.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.seven.joker.databinding.FeedTypeImageBinding;
import com.seven.joker.databinding.FeedTypeVideoBinding;
import com.seven.joker.model.Feed;

public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
    private Context context;
    private String category;

    public FeedAdapter(Context context,String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
        this.category = category;
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;
        if (viewType == Feed.TYPE_IMAGE) {
            binding = FeedTypeImageBinding.inflate(LayoutInflater.from(context));

        } else if (viewType == Feed.TYPE_VIDEO) {
            binding = FeedTypeVideoBinding.inflate(LayoutInflater.from(context));
        }
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public void bindData(Feed item) {
            if (binding instanceof FeedTypeImageBinding) {
                FeedTypeImageBinding imageBinding = (FeedTypeImageBinding) binding;
                imageBinding.setFeed(item);
                imageBinding.feedImage.bindData(item.width,item.height,16,item.cover);
            } else if (binding instanceof FeedTypeVideoBinding) {
                FeedTypeVideoBinding videoBinding = (FeedTypeVideoBinding) binding;
                videoBinding.setFeed(item);
                videoBinding.playerView.bindData(category,item.width,item.height,item.cover,item.url);
            }

        }
    }
}
