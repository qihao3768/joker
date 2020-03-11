package com.seven.joker.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.seven.joker.R;
import com.seven.joker.model.User;
import com.seven.joker.network.ApiResponse;
import com.seven.joker.network.ApiService;
import com.seven.joker.network.JsonCallback;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private View close_button;
    private View login_button;
    private Tencent tencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        close_button = findViewById(R.id.action_close);
        login_button = findViewById(R.id.action_login);
        close_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_close) {
            finish();
        } else if (v.getId() == R.id.action_login) {
            login();
        }

    }

    private void login() {
        if (tencent == null) {
            tencent = Tencent.createInstance("101860444", getApplicationContext());
        }

        tencent.login(this, "all", loginListener);
    }

    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject response = (JSONObject) o;
            try {
                String openid = response.getString("openid");
                String access_token = response.getString("access_token");
                String expires_in = response.getString("expires_in");
                long expires_time = response.getLong("expires_time");
                tencent.setAccessToken(access_token, expires_in);
                tencent.setOpenId(openid);
                QQToken qqToken = tencent.getQQToken();
                getUserInfo(qqToken, openid, expires_time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(), "登录失败" + uiError.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
        }
    };

    private void getUserInfo(QQToken qqToken, String openid, long expires_time) {
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;
                try {
                    String nickname = response.getString("nickname");
                    String figureurl_2 = response.getString("figureurl_2");

                    save(nickname, figureurl_2, openid, expires_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), "登录失败" + uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save(String nickname, String avatar, String openid, long expires_time) {
        ApiService.get("/user/insert")
                .addParam("name", nickname)
                .addParam("avatar", avatar)
                .addParam("qqOpenId", openid)
                .addParam("expires_time", expires_time)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (response.body != null) {
                            UserManager.get().save((User) response.body);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ApiResponse response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "登录失败" + response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }
}
