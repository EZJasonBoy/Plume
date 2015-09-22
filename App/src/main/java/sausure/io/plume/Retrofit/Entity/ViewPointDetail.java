package sausure.io.plume.Retrofit.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JOJO on 2015/9/20.
 */
public class ViewPointDetail
{
    @SerializedName("body")
    private String body;

    @SerializedName("image")
    private String image;

    @SerializedName("css")
    private List<String> css;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
