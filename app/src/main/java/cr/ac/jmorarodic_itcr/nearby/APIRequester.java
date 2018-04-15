package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class APIRequester {
    private String url;
    private ArrayList<String> parameters;
    private ArrayList<String> parametersValues;
    private RequestQueue queue;
    private StringRequest jsonRequest;
    private Context context;
    private static APIRequester apiRequester = null;
    private JSONObject jsonObj;
    private boolean ready = false;

    public static APIRequester initInstance(String url,Context context){
        if(apiRequester != null)
            return apiRequester;
        else
            return new APIRequester(url,context);
    }

    public APIRequester getInstance(){
        if(apiRequester != null)
            return apiRequester;
        else
            return new APIRequester();
    }

    private APIRequester(){

    }

    private APIRequester(String url,Context context){
        this.url = url;

        this.context = context;
        queue = Volley.newRequestQueue(context);
        jsonObj = new JSONObject();
        jsonRequest = null;
    }

    public JSONObject getJson(final JSONObject jsonBody){
        final String requestBody = jsonBody.toString();

        jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",response);
                            Log.i("Response",getJsonObj().toString());
                            ready = true;
                        } catch (JSONException e) {
                            Log.i("ResponseError",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response",error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.i("Response","GetHeaders");
                HashMap<String, String> headers = new HashMap<String, String>();
                try {
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization","Token "+jsonBody.getString("key"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return headers;
            }

        };
        queue.add(jsonRequest);
        Log.i("Return",jsonObj.toString());
        return jsonObj;
    }

    public JSONObject getJsonObj() {
        return jsonObj;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public StringRequest getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(StringRequest jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
