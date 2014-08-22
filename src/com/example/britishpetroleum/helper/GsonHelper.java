
package com.example.britishpetroleum.helper;

import com.google.gson.Gson;

public class GsonHelper {

    public static Gson mGson = new Gson();

    public static Object getGson(String json, Class<?> class1) {

        return mGson.fromJson(json, class1);
    }

}
