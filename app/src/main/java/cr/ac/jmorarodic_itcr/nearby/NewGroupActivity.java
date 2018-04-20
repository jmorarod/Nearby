package cr.ac.jmorarodic_itcr.nearby;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class NewGroupActivity extends AppCompatActivity {

    private ImageView image;
    private static final int MY_PERMISSION_REQUEST = 1;
    private String categoriaID;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    String api_key;

    public void onClickGuardar(View view){
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            postJson(jsonRequestBody,"https://nearbyrestapi.herokuapp.com/restapi/api/grupos/crear");
            Toast.makeText(this,"Grupo Agregado con éxito",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onClickGuardarGrupo(View view){
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            postJson(jsonRequestBody,getString(R.string.url_crear_evento));

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Categoria");
        categoriaID = message;
        getPermissions();

        image= findViewById(R.id.imgEvent);
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


    public void getPermissions() {
        if(ContextCompat.checkSelfPermission(NewGroupActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(NewGroupActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(NewGroupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
            else {

                ActivityCompat.requestPermissions(NewGroupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }
        else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {
            case MY_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    if(ContextCompat.checkSelfPermission(NewGroupActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    public void postJson(final JSONObject jsonBody, String url){
        final JSONObject jsonObj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        final String requestBody = jsonBody.toString();

        jsonRequest = new StringRequest(Request.Method.POST, url,
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
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parameters = new HashMap<String, String>();

                EditText txtNombre = findViewById(R.id.txtTitle);
                EditText txtDescripcion = findViewById(R.id.txtDescription);
                parameters.put("nombre",  txtNombre.getText().toString());
                parameters.put("descripcion",  txtDescripcion.getText().toString());
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user", "2");
                parameters.put("persona",user);
                parameters.put("categoria",categoriaID);

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
}
