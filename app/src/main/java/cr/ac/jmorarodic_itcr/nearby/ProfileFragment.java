package cr.ac.jmorarodic_itcr.nearby;


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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();

    Bitmap bitmap;
    StringRequest jsonRequest;
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

        ArrayList<ListItemProfile> listaGrupos = new ArrayList<>();
        username = RootView.findViewById(R.id.txtUsername);
        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.sports, "Titulo de noticia");
            listaGrupos.add(l);
        }

        //Carga en listview
        final ListView listView = (ListView) RootView.findViewById(R.id.listGroup);
        ListItemProfileAdapter listItemProfile = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaGrupos);
        listView.setAdapter(listItemProfile);


        ArrayList<ListItemProfile> listaEventos = new ArrayList<>();

        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.photo, "Titulo de noticia");
            listaEventos.add(l);
        }

        //Carga en listview
        final ListView listView2 = (ListView) RootView.findViewById(R.id.listEvent);
        ListItemProfileAdapter listItemProfile2 = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaEventos);
        listView2.setAdapter(listItemProfile2);

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

//            Log.i("json",jsonResponse.toString());
        } catch (JSONException e) {
            Log.i("Error",e.toString());
            e.printStackTrace();
        }

        return RootView;
    }


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

            //TODO: cambiar por nombre
            username.setText(jsonObj.getString("correo"));
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

}
