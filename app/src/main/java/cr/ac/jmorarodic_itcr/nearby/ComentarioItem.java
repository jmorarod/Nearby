package cr.ac.jmorarodic_itcr.nearby;

import android.graphics.Bitmap;

/**
 * Created by josem on 12/4/2018.
 */

public class ComentarioItem {
    private String userName;
    private String comentario;
    private Bitmap userImage;

    public ComentarioItem(String userName, String comentario, Bitmap userImage) {
        this.userName = userName;
        this.comentario = comentario;
        this.userImage = userImage;
    }

    public ComentarioItem(String userName, String comentario) {
        this.userName = userName;
        this.comentario = comentario;
        userImage = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
