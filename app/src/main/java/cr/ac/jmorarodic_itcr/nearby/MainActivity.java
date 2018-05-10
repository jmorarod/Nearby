package cr.ac.jmorarodic_itcr.nearby;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    private String defaultPassword;
    private String email;

    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    String api_key;
    String userid;

    public void onClickIngresar(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void onClickRegistrarse(View view)
    {
        //intent
        Intent intent = new Intent(this,RegistrarActivity.class);
        startActivity(intent);
        /*
        CharSequence opciones[] = new CharSequence[] {"Ingresar con la aplicación", "Facebook"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //intent
                    Log.i("Registro","RegistroApp");
                }
                else{
                    Log.i("Login","Facebook");

                }
            }
        });
        builder.show();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();

        defaultPassword = "12345asd";

        Log.i("TestMain","Test");
        //TODO: ver si está logueado, si no
        SharedPreferences sharedPreferences = this.getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);

        //TODO: Quitar comentario de la siguiente linea para quitar la sesión QUITELO CUANDO TERMINEMOS
        sharedPreferences.edit().putBoolean("logged",false).apply();
        if(sharedPreferences.getBoolean("logged",false)){
            Intent intent = new Intent(MainActivity.this,IndexActivity.class);
            startActivity(intent);
        }else{
            sharedPreferences.edit().putBoolean("logged",false).apply();
        }

      LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("Manager", "onSuccess");

                    }

                    @Override
                    public void onCancel() {
                        Log.i("Manager", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("Manager", "onError");
                    }
                });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                //TODO: datos de facebook pasarlos por el intent o guardarlos en shared preferences
                //TODO: verificar si existe la cuenta, si existe el intent es a la pantalla principal, si no entonces:
                //Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                //startActivity(intent);
                Log.i("LoginButton", "onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("MainActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("LoginButton", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("LoginButton", ""+exception);
            }
        });
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            email = object.getString("email");
            createUser();

            return bundle;
        }
        catch(JSONException e) {
            Log.d("ERROR","Error parsing JSON");
        }
        return null;
    }

    public void createUser()
    {
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            postJson(jsonRequestBody,"https://nearbyrestapi.herokuapp.com/rest-auth/registration/");

        } catch (JSONException e) {
            e.printStackTrace();
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

                            jsonObj.put("response",response);
                            Log.i("Response API",jsonObj.toString());
                            jsonResponse = jsonObj;
                            loadApiKey(jsonResponse);

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

                parameters.put("username", email);
                parameters.put("email", email);
                parameters.put("password1", defaultPassword);
                parameters.put("password2",  defaultPassword);


                return parameters;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                //headers.put("Authorization","Token "+jsonBody.getString("key"));

                return headers;
            }

        };
        queue.add(jsonRequest);

    }

    void loadApiKey(JSONObject jsonObject)
    {
        jsonRequest.cancel();
        ArrayList<String> response = new ArrayList<>();
        try {
            jsonObject = new JSONObject(jsonObject.getString("response"));
            api_key = jsonObject.getString("key");
            Log.i("JSON_API",api_key);
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            editor.putString("auth_token",api_key).apply();


            getUserid();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getUserid()
    {
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        api_key = sharedPreferences.getString("auth_token","");
        try {
            jsonRequestBody.put("key",api_key);
            getJson(jsonRequestBody,("https://nearbyrestapi.herokuapp.com/restapi/api/usuarioactual?user="+api_key));
        } catch (JSONException e) {
            e.printStackTrace();
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

                            jsonObj.put("response",new JSONArray(response));
                            Log.i("Response ID",jsonObj.toString());
                            jsonResponse = jsonObj;
                            loadUserid(jsonResponse);


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

    public void loadUserid(JSONObject jsonObject)
    {
        jsonRequest.cancel();
        ArrayList<String> response = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            userid = ""+ jsonArray.getJSONObject(0).getInt("user");
            Log.i("USER_ID",userid);
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("user", userid).apply();


            Intent intent = new Intent(this, CategoriasActivity.class);
            startActivity(intent);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
