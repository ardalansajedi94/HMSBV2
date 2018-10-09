package ir.hotelairport.androidapp.Models;

import java.util.ArrayList;

/**
 * Created by Mohammad on 9/2/2017.
 */

public class News {
    private int id;
   private String title,content;
    private ArrayList<Image>images;

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
