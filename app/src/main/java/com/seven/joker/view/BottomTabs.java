package com.seven.joker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.seven.joker.R;
import com.seven.joker.configs.AppConfig;
import com.seven.joker.model.BottomTab;
import com.seven.joker.model.Destination;
import com.seven.joker.utils.DeviceUtil;

import java.util.List;

public class BottomTabs extends BottomNavigationView {
    private static int[] icons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine};

    public BottomTabs(Context context) {
        this(context, null);
    }

    public BottomTabs(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public BottomTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BottomTab bottomTab = AppConfig.getBottomTabs();
        List<BottomTab.Tabs> tabs = bottomTab.tabs;
        //定义二维数组指定选中未选中状态
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        //定义一维数组显示状态颜色
        int[] colors = new int[]{Color.parseColor(bottomTab.activeColor), Color.parseColor(bottomTab.inActiveColor)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomTab.selectTab);
        //添加按钮图标
        for (int i = 0; i < tabs.size(); i++) {
            BottomTab.Tabs tab = tabs.get(i);
            if (!tab.enable) {
                return;
            }
            int id = getId(tab.pageUrl);
            if (id < 0) {
                return;
            }
            MenuItem item = getMenu().add(0, id, tab.index, tab.title);
            item.setIcon(icons[tab.index]);
        }
        //改变按钮尺寸
        for (int i = 0; i < tabs.size(); i++) {
            BottomTab.Tabs tab = tabs.get(i);
            int iconSize = DeviceUtil.dp2px(getContext(), tab.size);
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(tab.index);
            itemView.setIconSize(iconSize);
            if (TextUtils.isEmpty(tab.title)) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)));
                itemView.setShifting(false);
            }
        }
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getDestination().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.id;
    }
}
