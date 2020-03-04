package com.seven.joker.configs;

import android.content.ComponentName;

import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.seven.joker.model.Destination;

import java.util.HashMap;

public class NavGraphBuider {
    public static void build(NavController navController) {
        NavigatorProvider provider = navController.getNavigatorProvider();
        FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
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
                activityDestination.setComponentName(new ComponentName(QiApplication.getInstance().getPackageName(),value.className));
                activityDestination.setId(value.id);
                activityDestination.addDeepLink(value.pageUrl);
                navGraph.addDestination(activityDestination);
            }
            if (value.isStart){
                navGraph.setStartDestination(value.id);
            }
        }
        navController.setGraph(navGraph);
    }
}
