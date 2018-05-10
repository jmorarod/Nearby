package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView image;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();

    Bitmap bitmap;
    StringRequest jsonRequest;
    String api_key;
    Map map = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        image = findViewById(R.id.profile_image);
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        String user = sharedPreferences.getString("user","2");

        try {

            jsonRequestBody.put("key",api_key);
            getJson(jsonRequestBody,getString(R.string.url_usuario_detalle)+"/"+user+"/detalle");

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            Log.i("Error",e.toString());
            e.printStackTrace();
        }
    }

    public void onGuardarClicked(View view) {
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");

        sharedPreferences = getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "2");

        String link = "https://nearbyrestapi.herokuapp.com/restapi/api/usuarios/"+user+"/editar";



        try {
            jsonRequestBody.put("key",api_key);
            postJson(jsonRequestBody,link);

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void postJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        final String requestBody = jsonBody.toString();

        jsonRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response",response);

                        try {
                            Log.i("Response","Writing Json");

                            jsonObj.put("response",new JSONArray(response));
                            Log.i("Response",jsonObj.toString());
                            jsonResponse = jsonObj;
                            //loadCategorias(jsonResponse);

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
                EditText txtName = findViewById(R.id.txtName);
                //EditText txtLugar = findViewById(R.id.txtLocation);
                //EditText txtDescripcion = findViewById(R.id.txtDescription);
                parameters.put("nombre", txtName.getText().toString());
                //parameters.put("lugar", txtLugar.getText().toString());
                //parameters.put("descripcion",  txtDescripcion.getText().toString());

                if(map != null) {
                    Log.i("Foto",(String) map.get("secure_url"));
                    parameters.put("foto", (String) map.get("secure_url"));
                    Log.i("map not null",(String) map.get("secure_url"));
                }
                else
                    parameters.put("foto", "");
                //parameters.put("nombre", String.valueOf());
                parameters.put("fecha_nacimiento", "");
                parameters.put("genero", "");
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user", "2");
                //parameters.put("persona",user);
                //parameters.put("categoria",categoriaID);
                //parameters.put("calificacion","5");
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
        queue.add(jsonRequest);

    }

    public void onClickBack(View view)
    {
        Intent intent = new Intent(this,IndexActivity.class);
        startActivity(intent);
    }

    public void onClickImagen(View view)
    {
        cargarImagen();
    }

    public void cargarImagen()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            Uri path = data.getData();
            image.setImageURI(path);
        }
    }

    public void getJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

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
        ArrayList<String> urls = new ArrayList<>();
        try {
                JSONObject jsonObj = jsonObject.getJSONObject("response");
                String url = jsonObj.getString("foto");



                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                Log.i("url",url);
                bitmap = imageDownloadTask.execute(url).get();
                image.setImageBitmap(bitmap);

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
            }
            return null;
        }
    }

    class ImageUploadTask extends AsyncTask<Uri,Void,Bitmap> {
        File file;
        FileInputStream fileInputStream;
        @Override
        protected Bitmap doInBackground(Uri... urls) {

            Map config = new HashMap();
            config.put("cloud_name", "poppycloud");
            config.put("api_key", "328358331617938");
            config.put("api_secret", "z-7k70XpvP1dl1ZdiqVF0olXp7A");

            Cloudinary cloudinary = new Cloudinary(config);

            file = new File(String.valueOf(urls[0]));
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Log.i("URI",urls[0].getPath());
                String path = getRealPathFromUri(getApplicationContext(),urls[0]);
                Log.i("Path",path);
                map = cloudinary.uploader().upload(path, ObjectUtils.emptyMap() );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
