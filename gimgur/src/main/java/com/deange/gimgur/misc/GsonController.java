package com.deange.gimgur.misc;

import com.deange.gimgur.model.ImageResult;
import com.deange.gimgur.model.QueryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonController {

    private static final Object sLock = new Object();
    private static Gson sCachedFactory;
    private Gson mGson;
    private static GsonController sInstance;

    public synchronized static GsonController getInstance() {
        if (sInstance == null) {
            sInstance = new GsonController();
        }

        return sInstance;
    }

    public synchronized static Gson getCachedFactory() {
        if (sCachedFactory == null) {
            sCachedFactory = new Gson();
        }
        return sCachedFactory;
    }

    private GsonController() {

        mGson = new GsonBuilder()
                .registerTypeAdapter(QueryResponse.class, new QueryDeserializer())
                .create();
    }

    public String to(final Object src) {
        return mGson.toJson(src);
    }

    public <T> T from(final String json, final Class<T> classOfT) throws JsonSyntaxException {
        return mGson.fromJson(json, classOfT);
    }

    public <T> T from(final String json, final Type type) throws JsonSyntaxException {
        return mGson.fromJson(json, type);
    }

    public static class QueryDeserializer implements JsonDeserializer<QueryResponse> {

        @Override
        public QueryResponse deserialize(final JsonElement jsonElement, final Type type,
                                         final JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {

            final JsonObject root = jsonElement.getAsJsonObject();
            final JsonObject data = root.getAsJsonObject("responseData");
            final JsonObject cursor = data.getAsJsonObject("cursor");
            final JsonArray results = data.getAsJsonArray("results");

            final Type listType = new TypeToken<List<ImageResult>>(){}.getType();
            final List<ImageResult> images = GsonController.getCachedFactory().fromJson(results, listType);

            // Create the deserialized response object
            final QueryResponse response = GsonController.getCachedFactory().fromJson(root, QueryResponse.class);
            response.setImages(images);
            response.setNextUrl(cursor.get("moreResultsUrl").getAsString());

            return response;
        }
    }
}
