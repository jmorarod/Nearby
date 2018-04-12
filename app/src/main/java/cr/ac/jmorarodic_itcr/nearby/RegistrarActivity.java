package cr.ac.jmorarodic_itcr.nearby;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class RegistrarActivity extends AppCompatActivity {
    private int nDay;
    private int nMonth;
    private int nYear;

    public void onClickRegistrar(View view){

        EditText correoText = (EditText)findViewById(R.id.correoText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);
        EditText cPasswordText = (EditText)findViewById(R.id.confirmPasswordText);
        Spinner spinner = findViewById(R.id.spinner2);
        String correo = correoText.getText().toString();

        String password = passwordText.getText().toString();
        String confirmPassword = cPasswordText.getText().toString();
        Log.i("passwords","password: "+password +" confirm: "+confirmPassword);
        if(password.equals(confirmPassword)){
            String genero = spinner.getSelectedItem().toString();
            String fecha = nMonth +"/"+nDay+"/"+nYear;
            if(correo != "" && password != "" && confirmPassword != "" && genero != "" && fecha != "0/0/0") {
                //TODO: Registrar en backend
                Log.i("usuario", correo + " " + password + " " + genero + " " + fecha);
            }else{
                Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this,"Los campos de contraseña y confirmar contraseña deben ser iguales",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_registrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }

}
