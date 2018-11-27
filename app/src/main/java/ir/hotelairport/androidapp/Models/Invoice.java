package ir.hotelairport.androidapp.Models;

/**
 * Created by Mohammad on 10/3/2017.
 */

public class Invoice {
    int has_response;
    String response, created_at, file_source;

    public int getHas_response() {
        return has_response;
    }

    public void setHas_response(int has_response) {
        this.has_response = has_response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFile_source() {
        return file_source;
    }

    public void setFile_source(String file_source) {
        this.file_source = file_source;
    }
}
