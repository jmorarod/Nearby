package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity {

    private ImageView image;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    LatLng placeLocation;
    Uri path;
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    Map map;
    StringRequest jsonRequest;
    String api_key;


    public void onClickGuardarEvento(View view){
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            postJson(jsonRequestBody,getString(R.string.url_listar_categorias));

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onClickImgLocation(View view){
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("CR")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                TextView txtLocation = findViewById(R.id.txtLocation);
                txtLocation.setText(place.getName());
                placeLocation = place.getLatLng();
                Log.i("PlaceLocation",placeLocation.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else{
            if(resultCode==RESULT_OK)
            {
                path = data.getData();
                image.setImageURI(path);
                Map config = new HashMap();
                config.put("cloud_name", "poppycloud");
                config.put("api_key", "328358331617938");
                config.put("api_secret", "z-7k70XpvP1dl1ZdiqVF0olXp7A");

                Cloudinary cloudinary = new Cloudinary(config);
                try {
                    map = cloudinary.uploader().upload(path, ObjectUtils.emptyMap() );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        image=findViewById(R.id.imgEvent);
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
            public Map<String, String> getParams() throws  AuthFailureError{
                HashMap<String, String> parameters = new HashMap<String, String>();
                TextView txtFecha = findViewById(R.id.txtFecha);
                TextView txtLugar = findViewById(R.id.txtLocation);
                TextView txtDescripcion = findViewById(R.id.txtDescription);
                parameters.put("fecha", (String) txtFecha.getText());
                parameters.put("lugar", (String) txtLugar.getText());
                parameters.put("descripcion", (String) txtDescripcion.getText());
                parameters.put("imagen", (String) map.get("secure_url"));
                parameters.put("latitud", String.valueOf(placeLocation.latitude));
                parameters.put("longitud", String.valueOf(placeLocation.longitude));
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user", "2");
                parameters.put("persona",user);
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
