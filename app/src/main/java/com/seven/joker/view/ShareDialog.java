package com.seven.joker.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seven.joker.R;
import com.seven.joker.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class ShareDialog extends AlertDialog {
    List<ResolveInfo> shareitems = new ArrayList<>();
    private ShareAdapter adapter;
    private String shareContent;
    private View.OnClickListener listener;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        RoundFrameLayout layout = new RoundFrameLayout(getContext());
        layout.setBackgroundColor(Color.WHITE);
        layout.setViewOutLine(DeviceUtil.dp2px(20), ViewHelper.RADIUS_TOP);
        RecyclerView gridView = new RecyclerView(getContext());
        gridView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new ShareAdapter();
        gridView.setAdapter(adapter);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = DeviceUtil.dp2px(20);
        params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = margin;
        params.gravity = Gravity.CENTER;
        layout.addView(gridView, params);
        setContentView(layout);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        queryShareItems();
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public void setShareitemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private void queryShareItems() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, "com.tencent.mm") || TextUtils.equals(packageName, "com.tencent.mobileqq")) {
                shareitems.add(resolveInfo);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final PackageManager packageManager;

        public ShareAdapter() {
            packageManager = getContext().getPackageManager();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_share, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ResolveInfo resolveInfo = shareitems.get(position);
            QiImageView shareicon = holder.itemView.findViewById(R.id.share_icon);
            Drawable drawable = resolveInfo.loadIcon(packageManager);
            shareicon.setImageDrawable(drawable);
            TextView sharetext = holder.itemView.findViewById(R.id.share_text);
            sharetext.setText(resolveInfo.loadLabel(packageManager));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pkgName = resolveInfo.activityInfo.packageName;
                    String clsName = resolveInfo.activityInfo.name;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setComponent(new ComponentName(pkgName, clsName));
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    getContext().startActivity(intent);
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return shareitems == null ? 0 : shareitems.size();
        }
    }
}
