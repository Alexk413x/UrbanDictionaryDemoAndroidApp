package com.alexk413x.mobile.urbandictionarydemo.API;

import com.alexk413x.mobile.urbandictionarydemo.Interfaces.GetWordDefinitionsListener;
import com.alexk413x.mobile.urbandictionarydemo.Models.WordDefinition;
import com.alexk413x.mobile.urbandictionarydemo.Utils.DateUtils;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.apiHost;
import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.apiHostId;
import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.apiKey;
import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.apiKeyId;
import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.baseUrl;
import static com.alexk413x.mobile.urbandictionarydemo.API.ApiManager.httpQueue;

public class WordDefinitionApi {

    private static final String apiUrl = "/define?term=";

    //One definition test case "dfghjkl;"
    //no results and one word returns list[]

    public static void get(String word, final GetWordDefinitionsListener wordListener) {

        String url = baseUrl + apiUrl + word;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray definitionsJson = response.getJSONArray("list");
                            ArrayList<WordDefinition> definitionsList = new ArrayList<>();

                            for (int i = 0; i < definitionsJson.length(); i++) {
                                JSONObject defJson = definitionsJson.getJSONObject(i);

                                String word = defJson.getString("word");
                                String definition = defJson.getString("definition");
                                int thumbsUp = defJson.getInt("thumbs_up");
                                int thumbsDown = defJson.getInt("thumbs_down");
                                int definitionId = defJson.getInt("defid");
                                String author = defJson.getString("author");
                                Calendar createdDate = new DateUtils().createDateFromString(defJson.getString("written_on"));
                                String permalink = defJson.getString("permalink");
                                String example = defJson.getString("example");
                                String currentVote = defJson.getString("current_vote");

                                JSONArray soundUrlJsonArray = defJson.getJSONArray("sound_urls");
                                ArrayList<String> soundUrls = new ArrayList<>();
                                for(int j=0; j<soundUrlJsonArray.length(); j++){
                                    soundUrls.add(soundUrlJsonArray.getString(j));
                                }

                                definitionsList.add(new WordDefinition(word, definition, thumbsUp, thumbsDown, definitionId, author, createdDate, permalink, example, currentVote, soundUrls));
                            }

                            wordListener.getResult(definitionsList);
                        } catch (JSONException e) {
                            wordListener.jsonException(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                wordListener.getError(error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(apiHostId, apiHost);
                params.put(apiKeyId, apiKey);
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    long currentTime = System.currentTimeMillis();
                    final long cacheRefreshTime = 5 * 60 * 1000;
                    final long cacheExpiredTime = 24 * 60 * 60 * 1000;
                    final long cacheRefreshTimeFromNow = currentTime + cacheRefreshTime;
                    final long cacheExpireTimeFromNow = currentTime + cacheExpiredTime;

                    String headerDate = response.headers.get("Date");
                    String headerLastModified = response.headers.get("Last-Modified");

                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);

                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = cacheRefreshTimeFromNow;
                    cacheEntry.ttl = cacheExpireTimeFromNow;

                    if (headerDate != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerDate);
                    }

                    if (headerLastModified != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerLastModified);
                    }

                    cacheEntry.responseHeaders = response.headers;

                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    return Response.success(new JSONObject(jsonString), cacheEntry);

                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            public void addMarker(String tag) {
                super.addMarker(tag);
                //cache-hit-refresh-needed
                if (tag.equals("cache-hit-refresh-needed") || tag.equals("cache-hit-expired")){
                    wordListener.isCacheRefresh();
                }
            }
        };

        httpQueue.add(jsonObjectRequest);
    }
}
