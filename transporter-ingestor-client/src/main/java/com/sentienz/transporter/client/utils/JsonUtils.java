package com.sentienz.transporter.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
  private static Gson gson = null;

  public static Gson getGson() {
    if (gson == null) {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gson = gsonBuilder.create();
    }
    return gson;
  }

  public static String toJson(Object obj) {
    return getGson().toJson(obj);
  }

  public static <T> T fromJson(String json, Class<T> classOfT) {
    return JsonUtils.getGson().fromJson(json, classOfT);
  }

}
