package com.seven.joker.configs;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.seven.joker.model.BottomTab;
import com.seven.joker.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppConfig {
    private static HashMap<String, Destination> destination;
    private static BottomTab bottomTabs;

    public static HashMap<String, Destination> getDestination() {
        if (destination == null) {
            String content = parseFile("destination.json");
            destination = JSON.parseObject(content, new TypeReference<HashMap<String, Destination>>() {
            }.getType());
        }
        return destination;
    }

    public static BottomTab getBottomTabs() {
        if (bottomTabs == null) {
            String content = parseFile("bottom.json");
            bottomTabs = JSON.parseObject(content, BottomTab.class);
        }
        return bottomTabs;
    }


    //解析assets下的.json文件方法
    private static String parseFile(String fileName) {
        AssetManager assets = QiApplication.getInstance().getResources().getAssets();
        InputStream is = null;
        BufferedReader br = null;
        String line = null;
        StringBuilder builder = new StringBuilder();
        try {
            is = assets.open(fileName);
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return builder.toString();
    }
}
