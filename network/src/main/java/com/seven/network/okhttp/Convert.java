package com.seven.network.okhttp;

import java.lang.reflect.Type;

public interface Convert<T> {
    T convert(String response, Type type);
}
