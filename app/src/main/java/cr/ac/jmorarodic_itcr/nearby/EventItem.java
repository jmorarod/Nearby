package cr.ac.jmorarodic_itcr.nearby;

import android.graphics.Bitmap;

/**
 * Created by karizp on 13/04/2018.
 */

public class EventItem {
    private String date;
    private String username;
    private String stars;
    private String title;
    private String description;
    private Bitmap imageCategorie;
    private int imageProfile;
    private String lat;
    private String lon;


    public EventItem(String date, String username, String stars, String title, String description, Bitmap imageCategorie, int imageProfile, String lat, String lon) {
        this.date = date;
        this.username = username;
        this.stars = stars;
        this.title = title;
        this.description = description;
        this.imageCategorie = imageCategorie;
        this.imageProfile = imageProfile;
        this.lat = lat;
        this.lon = lon;
    }

    public int getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(int imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImageCategorie() {
        return imageCategorie;
    }

    public void setImageCategorie(Bitmap imageCategorie) {
        this.imageCategorie = imageCategorie;
    }
}
