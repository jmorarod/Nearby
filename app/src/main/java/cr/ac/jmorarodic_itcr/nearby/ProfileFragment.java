package cr.ac.jmorarodic_itcr.nearby;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    JSONObject jsonResponse2;
    JSONObject jsonResponse3;
    JSONObject jsonResponse4;
    ArrayList<ListItemProfile> listaGrupos = new ArrayList<>();
    ArrayList<ListItemProfile> listaEventos = new ArrayList<>();
    RequestQueue queue;
    ListView listView;
    ListView listView2;
    Bitmap bitmap;
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    StringRequest jsonRequest3;
    StringRequest jsonRequest4;
    String api_key;
    CircleImageView image;
    TextView username;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);


        username = RootView.findViewById(R.id.txtUsername);
        listView = (ListView) RootView.findViewById(R.id.listGroup);
        listView2 = (ListView) RootView.findViewById(R.id.listEvent);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        listaEventos = new ArrayList<>();
        /*
        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.sports, "Titulo de noticia");
            listaGrupos.add(l);
        }
        */
        //Carga en listview



        /*
        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.photo, "Titulo de noticia");
            listaEventos.add(l);
        }*/




        ImageView edit = RootView.findViewById(R.id.imgEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView settings = RootView.findViewById(R.id.imgSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),SettingsNewActivity.class);
                startActivity(intent);
            }
        });
        image = RootView.findViewById(R.id.profile_image);
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        String user = sharedPreferences.getString("user","2");

        try {

            jsonRequestBody.put("key",api_key);
            getJson(jsonRequestBody,getString(R.string.url_usuario_detalle)+"/"+user+"/detalle");
            //getGruposJson(jsonRequestBody, getString(R.string.url_usuario_grupos)+"/"+user+"/"+"detalle");
            //getEventosJson(jsonRequestBody, getString(R.string.url_listar_eventos));
            getEventosUJson(jsonRequestBody,getString(R.string.url_usuario_detalle)+"/"+user+"/detalle");
            getGruposUJson(jsonRequestBody,getString(R.string.url_usuario_detalle)+"/"+user+"/detalle");

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            Log.i("Error",e.toString());
            e.printStackTrace();
        }

        return RootView;
    }


    public void getJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();


        final String requestBody = jsonBody.toString();

        jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONObject(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse = jsonObj;
                            loadUsuario(jsonResponse);


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

    }
    public void loadUsuario(JSONObject jsonObject){
        jsonRequest.cancel();
        try {
            JSONObject jsonObj = jsonObject.getJSONObject("response");
            String url = jsonObj.getString("foto");


            JSONObject jsonUser =jsonObj.getJSONObject("user");

            username.setText(jsonUser.getString("username"));
            ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
            Log.i("url",url);
            bitmap = imageDownloadTask.execute(url).get();
            image.setImageBitmap(bitmap);

            final ArrayList<String> urls = new ArrayList<>();




        }

        catch (JSONException e) {
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
            }catch (Exception e){
                Log.i("exception",e.toString());
            }
            return null;
        }
    }
    public void getGruposJson(final JSONObject jsonBody, String url){
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
                            loadGrupos(jsonResponse);


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
    public void loadGrupos(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest.cancel();
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> nombres = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                JSONObject grupo = row.getJSONObject("grupo");
                urls.add(grupo.getString("foto"));
                nombres.add(grupo.getString("nombre"));


            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                ListItemProfile l = new ListItemProfile(bitmap, nombres.get(i));
                listaGrupos.add(l);
            }

            ListItemProfileAdapter listItemProfile = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaGrupos);
            listView.setAdapter(listItemProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void getEventosJson(final JSONObject jsonBody, String url){
        Log.i("GetJson","Get Eventos");
        final JSONObject jsonObj = new JSONObject();

        final String requestBody = jsonBody.toString();

        jsonRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONArray(response));
                            Log.i("Get Eventos",jsonObj.toString());
                            jsonResponse3 = jsonObj;
                            loadEventos(jsonResponse3);


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
        queue.add(jsonRequest2);

    }
    public void loadEventos(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest.cancel();
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> nombres = new ArrayList<>();
        Log.i("LoadEventos","LoadEventos");
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                JSONObject persona = row.getJSONObject("persona");
                SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user","2");
                if(persona.getString("id") == user) {
                    urls.add(row.getString("imagen"));
                    nombres.add(row.getString("descripcion"));
                }


            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                ListItemProfile l = new ListItemProfile(bitmap, nombres.get(i));
                listaEventos.add(l);
            }

            ListItemProfileAdapter listItemProfile = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaEventos);
            listView2.setAdapter(listItemProfile);
            listItemProfile.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void getEventosUJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final String requestBody = jsonBody.toString();

        jsonRequest3 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONObject(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse = jsonObj;
                            loadEventosU(jsonResponse);


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
        queue.add(jsonRequest3);

    }
    public void loadEventosU(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest3.cancel();
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

            JSONObject jsonObjj = jsonObject.getJSONObject("response");
            JSONArray jsonObj = jsonObjj.getJSONArray("eventos");
            for (int j = 0; j < jsonObj.length(); j++) {

                JSONObject row = jsonObj.getJSONObject(j);



                Log.i("Responseeeee",row.toString());
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
                ListItemProfile e = new ListItemProfile(bitmap, titulos.get(i));

                listaEventos.add(e);
            }

            ListItemProfileAdapter eventMainAdapter = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaEventos);
            listView2.setAdapter(eventMainAdapter);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    public void getGruposUJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final String requestBody = jsonBody.toString();

        jsonRequest4 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONObject(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse4 = jsonObj;
                            loadGruposU(jsonResponse4);


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
        queue.add(jsonRequest4);

    }
    public void loadGruposU(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest4.cancel();
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

            JSONObject jsonObjj = jsonObject.getJSONObject("response");
            JSONArray jsonObj = jsonObjj.getJSONArray("grupos");
            for (int j = 0; j < jsonObj.length(); j++) {

                JSONObject row = jsonObj.getJSONObject(j);



                Log.i("Responseeeee",row.toString());
                //fechas.add(row.getString("fecha"));
                //lugares.add(row.getString("lugar"));
                //users.add(row.getString("persona"));
                //puntuaciones.add(row.getString("calificacion"));
                titulos.add(row.getString("nombre"));
                //descripciones.add(row.getString("descripcion"));
                //idEventos.add(row.getString("id"));
                //latitudes.add(row.getString("latitud"));
                //longitudes.add(row.getString("longitud"));
                urls.add("http://res.cloudinary.com/poppycloud/image/upload/v1524042969/descarga_1.jpg");



            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                ListItemProfile e = new ListItemProfile(bitmap, titulos.get(i));

                listaGrupos.add(e);
            }

            ListItemProfileAdapter eventMainAdapter = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaGrupos);
            listView.setAdapter(eventMainAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}
