package cr.ac.jmorarodic_itcr.nearby;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ArrayList<Bitmap> mbitmaps = new ArrayList<>();
    ArrayList<String> categorias = new ArrayList<>();
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    String api_key;
    RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment*/

        View RootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Carga lista vertical categorias
        //Ejemplo de datos



        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView = (RecyclerView) RootView.findViewById(R.id.listMainCategorieView);
        recyclerView.setLayoutManager(layoutManager);
        mbitmaps = new ArrayList<>();
        categorias = new ArrayList<>();
        SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        if(categorias.size() == 0) {
            try {
                jsonRequestBody.put("key", api_key);

                String user = sharedPreferences.getString("user", "2");
                getJson(jsonRequestBody, getString(R.string.url_listar_categorias_usuario)+"?usuario="+user);

//            Log.i("json",jsonResponse.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        //Carga eventos disponibles
        //Solo son ejemplos de eventos
        ArrayList<EventItem> eventItems = new ArrayList<>();
        EventItem e = new EventItem("13 Marzo 2018", "Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.sports,R.drawable.profile_default);

        eventItems.add(e);

        e = new EventItem("13 Marzo 2018", "Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.photo,R.drawable.profile_default);

        eventItems.add(e);

        e = new EventItem("13 Marzo 2018", "Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.technology,R.drawable.profile_default);

        eventItems.add(e);

        //Carga en listview
        final ListView listView = (ListView) RootView.findViewById(R.id.listMainEvent);
        EventMainAdapter eventMainAdapter = new EventMainAdapter(getActivity().getApplicationContext(),R.layout.list_item_categories_main,eventItems);
        listView.setAdapter(eventMainAdapter);




        return RootView;




    }

    //Carga las categorias
    public void getJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final String requestBody = jsonBody.toString();

        jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONArray(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse = jsonObj;
                            loadCategorias(jsonResponse);


                        } catch (JSONException e) {
                            Log.i("ResponseError",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response",error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    }
    public void loadCategorias(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest.cancel();
        ArrayList<String> urls = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                JSONObject categoria = row.getJSONObject("categoria");
                urls.add(categoria.getString("foto"));
                categorias.add(categoria.getString("nombre"));
            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                mbitmaps.add(bitmap);
            }
            //String[] mCategories = {"Salud", "Tecnología","Naturaleza","Aprendizaje", "Fotografía","Deportes"};
            Integer[] mImages = {R.drawable.health,R.drawable.technology,R.drawable.nature,R.drawable.learn,R.drawable.photo,R.drawable.sports};
            String[] mCategories = new String[categorias.size()];
            Bitmap[] bitmaps = new Bitmap[mbitmaps.size()];
            for(int i = 0; i < categorias.size(); i++)
            {
                mCategories[i] = categorias.get(i);
                bitmaps[i] = mbitmaps.get(i);
            }
            CategorieMainAdapter adapterC = new CategorieMainAdapter(mCategories,mImages,getActivity().getApplicationContext(),bitmaps);
            recyclerView.setAdapter(adapterC);
            adapterC.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    class ImageDownloadTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();

                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
