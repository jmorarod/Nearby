package cr.ac.jmorarodic_itcr.nearby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //Carga lista vertical categorias
        //Ejemplo de datos
        String[] mCategories = {"Salud", "Tecnología","Naturaleza","Aprendizaje", "Fotografía","Deportes"};
        Integer[] mImages = {R.drawable.health,R.drawable.technology,R.drawable.nature,R.drawable.learn,R.drawable.photo,R.drawable.sports};

        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listEventGroup);
        recyclerView.setLayoutManager(layoutManager);


        CategorieMainAdapter adapterC = new CategorieMainAdapter(mCategories,mImages,this);
        recyclerView.setAdapter(adapterC);



    }
}
