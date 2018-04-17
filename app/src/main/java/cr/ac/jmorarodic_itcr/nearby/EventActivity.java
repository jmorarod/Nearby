package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EventActivity extends AppCompatActivity {
    private Float lat;
    private Float lon;
    private String eventoID;
    ListView listView;
    JSONObject jsonResponse;
    JSONObject jsonResponse2;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    private Map map = null;
    String api_key;
    private ArrayList<ComentarioItem> comentarios = new ArrayList<>();
    public void onClickSend(View view){
        EditText editText = findViewById(R.id.txtComment);
        if(!editText.getText().toString().equals("")) {
            try {
                jsonRequestBody.put("key", api_key);
                postJson(jsonRequestBody, getString(R.string.url_post_comentario));
                cargarComentarios();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    public void onMapClick(View view){

        Intent intent = new Intent(this,MapsActivity.class);

        if(lat != null && lon != null) {
            intent.putExtra("LAT", lat);
            intent.putExtra("LON", lon);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        comentarios = new ArrayList<>();
        Intent intent = getIntent();

        /*byte[] byteArray = intent.getByteArrayExtra("image");
        ImageView imageView = findViewById(R.id.imageEvent);
        if(byteArray!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmp);
        }*/

        String url = intent.getStringExtra("Imagen");

        ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
        try {
            Bitmap bmp = imageDownloadTask.execute(url).get();
            ImageView imageView = findViewById(R.id.imageEvent);
            imageView.setImageBitmap(bmp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        lat = Float.parseFloat(intent.getStringExtra("Lat"));
        lon = Float.parseFloat(intent.getStringExtra("Lon"));

        TextView textTitulo = findViewById(R.id.txtTitle);
        textTitulo.setText(intent.getStringExtra("Titulo"));

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        float stars = Float.parseFloat(intent.getStringExtra("Calificacion"));
        if(stars < 0 || stars > 5){
            stars = 5;
        }
        ratingBar.setRating(stars);


        TextView txtFecha = findViewById(R.id.txtDate);
        txtFecha.setText(intent.getStringExtra("Fecha"));

        TextView txtLugar = findViewById(R.id.txtLocation);
        txtLugar.setText(intent.getStringExtra("Lugar"));

        TextView txtDescripcion = findViewById(R.id.txtDescription);
        txtDescripcion.setText(intent.getStringExtra("Descripcion"));

        eventoID = intent.getStringExtra("EventoID");


        listView = findViewById(R.id.listViewComentarios);



        cargarComentarios();


    }
    private void cargarComentarios(){
        comentarios = new ArrayList<>();
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key", api_key);

            String user = sharedPreferences.getString("user", "2");
            getJson(jsonRequestBody, getString(R.string.url_comentarios_listar));

        } catch (JSONException e) {
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

    public void getJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

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
                            loadComentarios(jsonResponse);


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
    public void loadComentarios(JSONObject jsonObject){
        Bitmap bitmap;
        jsonRequest.cancel();
        ArrayList<String> urls = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);

                if(row.getString("evento").equals(eventoID)) {
                    ComentarioItem comentarioItem = new ComentarioItem("Username", row.getString("contenido"), R.drawable.profile_default);

                    comentarios.add(comentarioItem);
                }
            }
            ComentarioAdapter categoriaAdapter=new ComentarioAdapter(this,R.layout.coments_list_view_item,comentarios);
            listView.setAdapter(categoriaAdapter);
            categoriaAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void postJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        final String requestBody = jsonBody.toString();

        jsonRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONArray(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse2 = jsonObj;

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
            public Map<String, String> getParams() throws  AuthFailureError{
                HashMap<String, String> parameters = new HashMap<String, String>();
                EditText editText = findViewById(R.id.txtComment);
                Log.i("Antes de contenido","Antes de contenido");
                parameters.put("contenido",editText.getText().toString());
                Log.i("Contenido",editText.getText().toString());
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user", "2");
                Log.i("User",user);
                Log.i("EventoID",eventoID);
                parameters.put("autor",user);
                parameters.put("evento",eventoID);
                return parameters;
            }
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

}
