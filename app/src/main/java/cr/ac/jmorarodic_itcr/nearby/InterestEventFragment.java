package cr.ac.jmorarodic_itcr.nearby;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class InterestEventFragment extends Fragment {

    ArrayList<Bitmap> mbitmaps = new ArrayList<>();
    ArrayList<EventCalendarItem> eventItems = new ArrayList<>();
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    String api_key;
    RecyclerView recyclerView;
    ListView listView;
    EventCalendarAdapter adapterC;
    Activity activity;



    public InterestEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_interest_event, container, false);

        eventItems = new ArrayList<>();
        mbitmaps = new ArrayList<>();


        //for(int i =0; i<10;i++) {
        //    EventCalendarItem e = new EventCalendarItem(R.drawable.health, "Titulo de noticia", "14 Abril 2018");
        //    eventos.add(e);
        //}
        SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "2");


        try {
            jsonRequestBody.put("key", api_key);
            getEventosJson(jsonRequestBody,getActivity().getString(R.string.url_event_interesado)+"?usuario="+user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView = (ListView) RootView.findViewById(R.id.interestEventList);
        EventCalendarAdapter eventMainAdapter = new EventCalendarAdapter(getActivity().getApplicationContext(),R.layout.list_item_event_calendar,eventItems);
        listView.setAdapter(eventMainAdapter);


        return RootView;
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
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public void getEventosJson(final JSONObject jsonBody, String url){
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
                            loadEventos(jsonResponse);


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
    public void loadEventos(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest.cancel();
        final ArrayList<String> urls = new ArrayList<>();
        final ArrayList<String> fechas = new ArrayList<>();
        final ArrayList<String> lugares = new ArrayList<>();
        ArrayList<String> users = new ArrayList<>();
        final ArrayList<String> puntuaciones = new ArrayList<>();
        final ArrayList<String> titulos = new ArrayList<>();
        final ArrayList<String> descripciones = new ArrayList<>();
        final ArrayList<String> idEventos = new ArrayList<>();
        final ArrayList<String> latitudes = new ArrayList<>();
        final ArrayList<String> longitudes = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject rowEvent = jsonArray.getJSONObject(i);
                JSONObject row = rowEvent.getJSONObject("evento");
                fechas.add(row.getString("fecha"));
                lugares.add(row.getString("lugar"));
                users.add(row.getString("persona"));
                puntuaciones.add(row.getString("calificacion"));
                titulos.add(row.getString("descripcion"));
                descripciones.add(row.getString("descripcion"));
                idEventos.add(row.getString("id"));
                latitudes.add(row.getString("latitud"));
                longitudes.add(row.getString("longitud"));
                urls.add(row.getString("imagen"));

            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                EventCalendarItem e = new EventCalendarItem(bitmap, titulos.get(i) , fechas.get(i));

                eventItems.add(e);
            }

            EventCalendarAdapter eventMainAdapter = new EventCalendarAdapter(getActivity().getApplicationContext(),R.layout.list_item_categories_main,eventItems);
            listView.setAdapter(eventMainAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),EventActivity.class);
                    intent.putExtra("Titulo",titulos.get(position));
                   /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bmp = eventItems.get(position).getImageCategorie();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("Bitmap", byteArray);*/
                    intent.putExtra("Imagen",urls.get(position));
                    intent.putExtra("Calificacion",puntuaciones.get(position));
                    intent.putExtra("Fecha",fechas.get(position));
                    intent.putExtra("Lugar",lugares.get(position));
                    intent.putExtra("Descripcion",descripciones.get(position));
                    intent.putExtra("EventoID",idEventos.get(position));
                    intent.putExtra("Lat",latitudes.get(position));
                    intent.putExtra("Lon",longitudes.get(position));
                    getActivity().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
