package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GroupActivity extends AppCompatActivity {
    private String[] eventosDescripciones;
    private String[] eventosImagenes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        TextView txtTitulo = findViewById(R.id.txtTitle);
        txtTitulo.setText(intent.getStringExtra("Titulo"));

        TextView txtDesripcion = findViewById(R.id.txtDescription);
        txtDesripcion.setText(intent.getStringExtra("Descripcion"));
        eventosDescripciones = intent.getStringArrayExtra("EventosDescripciones");
        Log.i("EventosImagenesLog",intent.getStringArrayExtra("EventosImagenes").toString());
        eventosImagenes = intent.getStringArrayExtra("EventosImagenes");



        String[] mCategories = eventosDescripciones;
        Integer[] mImages = {R.drawable.health,R.drawable.technology,R.drawable.nature,R.drawable.learn,R.drawable.photo,R.drawable.sports};
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for(int i = 0; i < eventosImagenes.length; i++){
            ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
            try {
                Bitmap bitmap = imageDownloadTask.execute(eventosImagenes[i]).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listEventGroup);
        recyclerView.setLayoutManager(layoutManager);

        Bitmap[] bmps = new Bitmap[bitmaps.size()];
        for(int i = 0; i < bitmaps.size(); i++){
            bmps[i] = bitmaps.get(i);
        }
       CategorieMainAdapter adapterC = new CategorieMainAdapter(mCategories,mImages,this, bmps,null);
       recyclerView.setAdapter(adapterC);



    }
    class ImageDownloadTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
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
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }
}
