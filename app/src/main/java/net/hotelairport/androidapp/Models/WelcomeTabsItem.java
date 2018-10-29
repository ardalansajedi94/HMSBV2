package net.hotelairport.androidapp.Models;

/**
 * Created by Mohammad on 8/24/2017.
 */

public class WelcomeTabsItem {
    private String title, content,image_source;

    public WelcomeTabsItem()
    {

    }
    public WelcomeTabsItem(String dealName, String dealDescription ) {
        this.title = dealName;
        this.content = dealDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String dealName) {
        this.title = dealName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
