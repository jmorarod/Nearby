package cr.ac.jmorarodic_itcr.nearby;

/**
 * Created by karizp on 13/04/2018.
 */

public class EventCalendarItem {
    private int image;
    private String title;
    private String date;

    public EventCalendarItem(int image, String title, String date) {
        this.image = image;
        this.title = title;
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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
