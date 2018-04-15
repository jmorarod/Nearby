package cr.ac.jmorarodic_itcr.nearby;

/**
 * Created by karizp on 14/04/2018.
 */

public class ListItemProfile {
    private int image;
    private String title;

    public ListItemProfile(int image, String title) {
        this.image = image;
        this.title = title;
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
}
