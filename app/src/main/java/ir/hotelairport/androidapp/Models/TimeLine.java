package ir.hotelairport.androidapp.Models;

import java.util.ArrayList;

/**
 * Created by Mohammad on 9/23/2017.
 */

public class TimeLine {
    private String content, date;
    private boolean is_response;
    private ArrayList<TimeLine> responses;

    public ArrayList<TimeLine> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<TimeLine> responses) {
        this.responses = responses;
    }

    public boolean is_response() {
        return is_response;
    }

    public void setIs_response(boolean is_response) {
        this.is_response = is_response;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
