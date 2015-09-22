package sausure.io.plume.Retrofit.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JOJO on 2015/9/10.
 */
public class ViewList
{
    @SerializedName("date")
    private String date;

    @SerializedName("stories")
    private List<ViewPoint> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ViewPoint> getStories() {
        return stories;
    }

    public void setStories(List<ViewPoint> stories) {
        this.stories = stories;
    }
}
