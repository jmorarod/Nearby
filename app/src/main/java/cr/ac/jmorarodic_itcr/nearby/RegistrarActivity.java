package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {
    private int nDay;
    private int nMonth;
    private int nYear;

    private  EditText correoText;
    private EditText passwordText;
    private EditText cPasswordText;
    private  EditText userText;

    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    String api_key;
    String userid;

    boolean login=false;


    public void onClickRegistrar(View view){
        correoText = (EditText)findViewById(R.id.correoText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        cPasswordText = (EditText)findViewById(R.id.confirmPasswordText);
        userText = findViewById(R.id.nombreText);
        String nombre = userText.getText().toString();
        Spinner spinner = findViewById(R.id.spinner2);
        String correo = correoText.getText().toString();

        String password = passwordText.getText().toString();
        String confirmPassword = cPasswordText.getText().toString();
        Log.i("passwords","password: "+password +" confirm: "+confirmPassword);
        if(password.equals(confirmPassword)){
            String genero = spinner.getSelectedItem().toString();
            String fecha = nMonth +"/"+nDay+"/"+nYear;
            if(correo != "" && password != "" && confirmPassword != "" && genero != "" && fecha != "0/0/0" && nombre != "") {
                //TODO: Registrar en backend
                Log.i("usuario", correo + " " + password + " " + genero + " " + fecha);

                createUser();





                Log.i("Usuario",""+userid);
                Log.i ("API", ""+api_key);



            }else{
                Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this,"Los campos de contraseña y confirmar contraseña deben ser iguales",Toast.LENGTH_SHORT).show();
        }

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
    public void onClickFecha(View view){
        RelativeLayout layout = findViewById(R.id.dateLayout);
        layout.setVisibility(View.VISIBLE);
    }

    public void onClickDateOk(View view){
        DatePicker datePicker = findViewById(R.id.datePicker);
        RelativeLayout layout = findViewById(R.id.dateLayout);
        layout.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_registrar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        RelativeLayout layout = findViewById(R.id.dateLayout);
        layout.setVisibility(View.INVISIBLE);

        DatePicker datePicker = findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR)-(calendar.get(Calendar.YEAR)%2000), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                nDay = dayOfMonth;
                nMonth = month;
                nYear = year;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Genero, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        correoText = findViewById(R.id.correoText);
        userText = findViewById(R.id.nombreText);
        passwordText = findViewById(R.id.passwordText);
        cPasswordText = findViewById(R.id.confirmPasswordText);


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

                parameters.put("username", correoText.getText().toString());
                parameters.put("email", correoText.getText().toString());
                parameters.put("password1", passwordText.getText().toString());
                parameters.put("password2",  cPasswordText.getText().toString());


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

            login=true;

            Intent intent = new Intent(this, CategoriasActivity.class);
            startActivity(intent);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
