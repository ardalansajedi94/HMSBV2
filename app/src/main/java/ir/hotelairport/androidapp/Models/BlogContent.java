package ir.hotelairport.androidapp.Models;

import java.util.ArrayList;

/**
 * Created by Mohammad on 9/1/2017.
 */

public class BlogContent {
    private int id, internalImage;
    private String title, content, created_at;
    private ArrayList<Image> images;
    private Category category;

    public BlogContent(String title) {
        this.title = title;
    }

    public BlogContent(String title, int InternalImage) {
        this.title = title;
        this.internalImage = InternalImage;
    }

    public int getInternalImage() {
        return internalImage;
    }

    public void setInternalImage(int internalImage) {
        this.internalImage = internalImage;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
