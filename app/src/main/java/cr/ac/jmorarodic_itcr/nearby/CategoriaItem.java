package cr.ac.jmorarodic_itcr.nearby;

import android.graphics.Bitmap;

/**
 * Created by josem on 11/4/2018.
 */

public class CategoriaItem {
    private String nombre;
    private Bitmap imagen;

    public CategoriaItem(String nombre, Bitmap imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
