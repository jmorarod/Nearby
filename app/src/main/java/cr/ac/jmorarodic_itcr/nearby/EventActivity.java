package cr.ac.jmorarodic_itcr.nearby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    ArrayList<ComentarioItem> comentariosItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        comentariosItems = new ArrayList<>();
        ListView listView = findViewById(R.id.listViewComentarios);
        //TODO: poblar comentariosItems con el Backend
        ComentarioAdapter categoriaAdapter=new ComentarioAdapter(this,R.layout.coments_list_view_item,comentariosItems);
        listView.setAdapter(categoriaAdapter);
    }
}
