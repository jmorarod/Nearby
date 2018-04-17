package cr.ac.jmorarodic_itcr.nearby;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
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
    ArrayList<String> idCategorias = new ArrayList<>();
    ArrayList<EventItem> eventItems = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationListener;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    String api_key;
    RecyclerView recyclerView;
    CategorieMainAdapter adapterC;
    ListView listView;
    private String lat = "9";
    private String lon = "-84";
    public CategorieMainAdapter getCategorieMainAdapter(){
        return adapterC;
    }
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment*/

        View RootView = inflater.inflate(R.layout.fragment_home, container, false);
        eventItems = new ArrayList<>();
        //Carga lista vertical categorias
        //Ejemplo de datos



        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView = (RecyclerView) RootView.findViewById(R.id.listMainCategorieView);
        recyclerView.setLayoutManager(layoutManager);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                lat = currentLocation.latitude+"";
                lon = currentLocation.longitude+"";
                if(adapterC!=null){
                    adapterC.setLat(lat);
                    adapterC.setLon(lon);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null) {
                lat = lastLocation.getLatitude() + "";
                lon = lastLocation.getLongitude() + "";
            }
            if(adapterC!=null){
                adapterC.setLat(lat);
                adapterC.setLon(lon);
            }


        }


        mbitmaps = new ArrayList<>();
        categorias = new ArrayList<>();
        SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");

        if(categorias.size() == 0) {
            try {
                jsonRequestBody.put("key", api_key);

                String user = sharedPreferences.getString("user", "2");
                getJson(jsonRequestBody, getString(R.string.url_listar_categorias_usuario)+"?usuario="+user);
                //getEventosJson(jsonRequestBody,getString(R.string.url_evento_cerca)+"?latitud="+lat+"&longitud="+lon+"&categoria="+categorias.get(Integer.parseInt(adapterC.getCurrentCategorie())-1));
//            Log.i("json",jsonResponse.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        //Carga eventos disponibles
        //Solo son ejemplos de eventos

        //Carga en listview
        listView = (ListView) RootView.findViewById(R.id.listMainEvent);
        //agregar onclicklistener





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
                idCategorias.add(categoria.getString("id"));
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
                Log.i("Categoria",categorias.get(i));
                bitmaps[i] = mbitmaps.get(i);
            }
            adapterC = new CategorieMainAdapter(mCategories,mImages,getActivity().getApplicationContext(),bitmaps, idCategorias);
            adapterC.setActivity(getActivity());
            adapterC.setLon(lon);
            adapterC.setLat(lat);
            adapterC.setEventItems(eventItems);
            adapterC.setListView(listView);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }

        }
    }
}
