package com.seven.joker.ui.login;

import android.content.Context;
import android.content.Intent;

import androidx.constraintlayout.solver.Cache;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.seven.joker.cache.CacheManager;
import com.seven.joker.model.User;

public class UserManager {
    private static final String KEY_CACHE_USER = "cache_user";
    private static UserManager userManager;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private User user;

    private UserManager() {
        User cache = (User) CacheManager.getCache(KEY_CACHE_USER);
        if (cache != null && cache.expires_time < System.currentTimeMillis()) {
            user = cache;
        }
    }

    public static UserManager get() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void save(User user) {
        this.user = user;
        CacheManager.save(KEY_CACHE_USER, user);
        if (userLiveData.hasObservers()) {
            userLiveData.postValue(user);
        }
    }

    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        return userLiveData;
    }

    public boolean isLogin() {
        return user == null ? false : user.expires_time < System.currentTimeMillis();
    }

    public User getUser() {
        return isLogin() ? user : null;
    }

    public long getUserId() {
        return isLogin() ? user.userId : 0;
    }
}
