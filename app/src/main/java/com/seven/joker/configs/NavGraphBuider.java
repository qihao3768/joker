package com.seven.joker.configs;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.seven.joker.QiApplication;
import com.seven.joker.model.Destination;

import java.util.HashMap;

public class NavGraphBuider {
    public static void build(NavController navController, FragmentActivity context, int containerId) {
        NavigatorProvider provider = navController.getNavigatorProvider();
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(context, context.getSupportFragmentManager(), containerId);
        provider.addNavigator(fragmentNavigator);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));
        HashMap<String, Destination> destination = AppConfig.getDestination();
        for (Destination value : destination.values()) {
            if (value.isFragment) {
                FragmentNavigator.Destination fragmentDestination = fragmentNavigator.createDestination();
                fragmentDestination.setClassName(value.className);
                fragmentDestination.setId(value.id);
                fragmentDestination.addDeepLink(value.pageUrl);
                navGraph.addDestination(fragmentDestination);
            } else {
                ActivityNavigator.Destination activityDestination = activityNavigator.createDestination();
                activityDestination.setComponentName(new ComponentName(QiApplication.getInstance().getPackageName(), value.className));
                activityDestination.setId(value.id);
                activityDestination.addDeepLink(value.pageUrl);
                navGraph.addDestination(activityDestination);
            }
            if (value.isStart) {
                navGraph.setStartDestination(value.id);
            }
        }
        navController.setGraph(navGraph);
    }
}
