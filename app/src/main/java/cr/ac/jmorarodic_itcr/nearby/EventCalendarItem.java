package cr.ac.jmorarodic_itcr.nearby;

import android.graphics.Bitmap;

/**
 * Created by karizp on 13/04/2018.
 */

public class EventCalendarItem {
    private Bitmap image;
    private String title;
    private String date;

    public EventCalendarItem(Bitmap image, String title, String date) {
        this.image = image;
        this.title = title;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}