package sausure.io.plume.Retrofit.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JOJO on 2015/9/10.
 */
public class ViewListItem implements Serializable
{
    @SerializedName("id")
    private int id;

    @SerializedName("images")
    private List<String> images;

    @SerializedName("title")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "id："+ id + ",title：" + title + ",image：" + images.get(0);
    }
}
