package sausure.io.plume.Retrofit.Entity;

import java.util.List;

/**
 * Created by JOJO on 2015/9/10.
 */
public class ViewList
{
    private String data;
    private List<ViewPoint> stories;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<ViewPoint> getStories() {
        return stories;
    }

    public void setStories(List<ViewPoint> stories) {
        this.stories = stories;
    }
}
