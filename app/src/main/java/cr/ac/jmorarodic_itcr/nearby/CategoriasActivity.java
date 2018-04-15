package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

public class CategoriasActivity extends AppCompatActivity {
    ArrayList<String> categorias = new ArrayList<String>();
    ArrayList<CategoriaItem> categoriasItems;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();

    Bitmap bitmap;
    GridView gridView;
    StringRequest jsonRequest;
    String api_key;

    public void onClickGrid(View view){

    }
    public void onClickContinuar(View view){
        Intent intent = new Intent(this,SubcategoriasActivity.class);
        intent.putExtra("Categorias",categorias.toArray());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        gridView = findViewById(R.id.gridViewCategorias);
        categoriasItems = new ArrayList<>();
        //TODO: cargar las categorias del backend y crear los Categoria Item para pasarlos al
        //apiRequester = APIRequester.initInstance(getString(R.string.url_listar_categorias),this);


        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            getJson(jsonRequestBody,getString(R.string.url_listar_categorias));

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(!categorias.contains(position+"")) {
                    categorias.add(position+"");
                    RelativeLayout relativeLayout = (RelativeLayout) gridView.getChildAt(position);
                    ImageView checkImage = (ImageView) relativeLayout.getChildAt(1);
                    checkImage.setVisibility(View.VISIBLE);
                }else{
                    categorias.remove(position+"");
                    RelativeLayout relativeLayout = (RelativeLayout) gridView.getChildAt(position);
                    ImageView checkImage = (ImageView) relativeLayout.getChildAt(1);
                    checkImage.setVisibility(View.INVISIBLE);
                }

                //adapter.removeitem(position)
                //adapter.notifyDataSetChanged
            }
        });
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
    public void loadCategorias(JSONObject jsonObject){
        jsonRequest.cancel();
        ArrayList<String> urls = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                urls.add(row.getString("foto"));
            }

            for(int i = 0; i < urls.size(); i++){
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
                bitmap = imageDownloadTask.execute(urls.get(i)).get();
                CategoriaItem categoriaItem = new CategoriaItem("",bitmap);
                categoriasItems.add(categoriaItem);
            }
            CategoriaAdapter categoriaAdapter=new CategoriaAdapter(this,R.layout.grid_view_items,categoriasItems);
            gridView.setAdapter(categoriaAdapter);
            categoriaAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    class ImageDownloadTask extends AsyncTask<String,Void,Bitmap>{

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


}