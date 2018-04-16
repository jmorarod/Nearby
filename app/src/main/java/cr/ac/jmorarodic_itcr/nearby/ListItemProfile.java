package cr.ac.jmorarodic_itcr.nearby;

import android.graphics.Bitmap;

/**
 * Created by karizp on 14/04/2018.
 */

public class ListItemProfile {
    private Bitmap image;
    private String title;

    public ListItemProfile(Bitmap image, String title) {
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
