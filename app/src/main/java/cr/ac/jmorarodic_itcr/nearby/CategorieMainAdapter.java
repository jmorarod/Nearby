package cr.ac.jmorarodic_itcr.nearby;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cr.ac.jmorarodic_itcr.nearby.R;

/**
 * Created by karizp on 11/04/2018.
 */

public class CategorieMainAdapter extends RecyclerView.Adapter<CategorieMainAdapter.ViewHolder>
{

    private String[] mCategories;
    private Integer[] mImages;
    private Context mContext;
    private Bitmap[] mImagesBitmaps;
    private ArrayList<String> mCategoriesID;
    private String currentCategorie;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    private String lat;
    private String lon;
    private Activity activity;
    private ListView listView;
    private ListView listView2;
    private ArrayList<EventItem> eventItems;
    private ArrayList<GroupItem> groupItems;
    public CategorieMainAdapter(String[] mCategories, Integer[] mImages, Context mContext, Bitmap[] mImagesBitmaps, ArrayList<String> mCategoriesID) {
        this.mCategories = mCategories;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mImagesBitmaps = mImagesBitmaps;
        this.mCategoriesID = mCategoriesID;
        currentCategorie = "1";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories_main,
                parent ,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.name.setText(mCategories[position]);
        //holder.image.setImageResource(mImages[position]);
        if(mImagesBitmaps.length!=0)
            holder.image.setImageBitmap(mImagesBitmaps[position]);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclickGroup","onClickGroup");
                Toast.makeText(mContext, mCategories[position], Toast.LENGTH_SHORT).show();
                try {
                    currentCategorie = mCategoriesID.get(position);
                    eventItems = new ArrayList<>();
                    groupItems = new ArrayList<>();
                    Log.i("onclickGroup","Before Try");
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                    String api_key = sharedPreferences.getString("auth_token", "");
                    jsonRequestBody.put("key", api_key);
                    try {
                        Log.i("onclickGroup","First Try");
                        getEventosJson(jsonRequestBody, getActivity().getString(R.string.url_evento_cerca) + "?latitud=" + lat + "&longitud=" + lon + "&categoria=" + mCategories[Integer.parseInt(currentCategorie) - 1]);
                    }catch (Exception ex){
                        Log.i("ExceptionRVEventos",ex.toString());
                    }

                    try{
                        Log.i("onclickGroup","Second Try");
                        getGruposJson(jsonRequestBody, getActivity().getString(R.string.url_grupos_por_categoria) + "?categoria=" + mCategories[Integer.parseInt(currentCategorie) - 1]);
                    }catch (Exception ex){
                        Log.i("ExceptionRV",ex.toString());
                    }
                }catch (Exception ex){
                    Log.i("onclickGroupGException",ex.toString());
                }
//
            }
        });
    }

    public String getCurrentCategorie(){
        return currentCategorie;
    }
    @Override
    public int getItemCount() {
        return mCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imgCategorie);
            this.name = itemView.findViewById(R.id.txtCategorie);
        }
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
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

                JSONObject row = jsonArray.getJSONObject(i);
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
                EventItem e = new EventItem(fechas.get(i), "Username", puntuaciones.get(i),titulos.get(i) , descripciones.get(i),
                        bitmap,R.drawable.profile_default, latitudes.get(i),latitudes.get(i));

                eventItems.add(e);
            }

            EventMainAdapter eventMainAdapter = new EventMainAdapter(activity.getApplicationContext(),R.layout.list_item_categories_main,eventItems);
            listView.setAdapter(eventMainAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(activity.getApplicationContext(),EventActivity.class);
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


    public void getGruposJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Log.i("RequestGrupos","RequestGrupos");
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
        final ArrayList<String> descripcionArray = new ArrayList<>();
        final ArrayList<String> url = new ArrayList<>();
        Log.i("LoadGrupos","LoadGrupos");
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject row = jsonArray.getJSONObject(i);

                String titulo = row.getString("descripcion");
                String descripcion = row.getString("descripcion");
                JSONArray arrayEventos = row.getJSONArray("eventosgrupo");

                for(int j =0; j < arrayEventos.length(); j++){
                    descripcionArray.add(arrayEventos.getJSONObject(j).getJSONObject("evento").getString("descripcion"));
                    url.add(arrayEventos.getJSONObject(j).getJSONObject("evento").getString("imagen"));
                    Log.i("URLGrupoEvento",url.get(i));
                }

                GroupItem g = new GroupItem(titulo, descripcion);
                groupItems.add(g);
            }


            final GroupMainAdapter groupMainAdapter = new GroupMainAdapter(activity.getApplicationContext(),R.layout.list_item_categories_main,groupItems);
            listView2.setAdapter(groupMainAdapter);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(activity.getApplicationContext(),GroupActivity.class);
                    intent.putExtra("Titulo",groupItems.get(position).getTitle());

                    String[] descripcionesStringArray = new String[descripcionArray.size()];
                    String[] urlStringArray = new String[url.size()];
                    for(int i = 0; i < url.size(); i++){
                        descripcionesStringArray[i] = descripcionArray.get(i);
                        urlStringArray[i] = url.get(i);
                    }
                    intent.putExtra("Descripcion",groupItems.get(position).getDescription());
                    intent.putExtra("EventosDescripciones",descripcionesStringArray);
                    intent.putExtra("EventosImagenes",urlStringArray);

                    getActivity().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public ArrayList<EventItem> getEventItems() {
        return eventItems;
    }

    public void setEventItems(ArrayList<EventItem> eventItems) {
        this.eventItems = eventItems;
    }

    public ListView getListView2() {
        return listView2;
    }

    public void setListView2(ListView listView2) {
        this.listView2 = listView2;
    }
}
