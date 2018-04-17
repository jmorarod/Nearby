package cr.ac.jmorarodic_itcr.nearby;

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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
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

        Log.i("TestMain","Test");
        //TODO: ver si está logueado, si no
        SharedPreferences sharedPreferences = this.getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);

        //Quitar comentario de la siguiente linea para quitar la sesión
        //sharedPreferences.edit().putBoolean("logged",false).apply();
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
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                //TODO: datos de facebook pasarlos por el intent o guardarlos en shared preferences
                //TODO: verificar si existe la cuenta, si existe el intent es a la pantalla principal, si no entonces:
                //Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
