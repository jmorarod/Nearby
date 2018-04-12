package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    ArrayList<ComentarioItem> comentariosItems;
    private Float lat;
    private Float lon;
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
        comentariosItems = new ArrayList<>();
        ListView listView = findViewById(R.id.listViewComentarios);
        //TODO: agarrar datos del evento del backend
        //TODO: poblar comentariosItems con el Backend
        ComentarioAdapter categoriaAdapter=new ComentarioAdapter(this,R.layout.coments_list_view_item,comentariosItems);
        listView.setAdapter(categoriaAdapter);
    }
}
