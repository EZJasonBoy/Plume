package sausure.io.plume.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sausure.io.plume.R;
import sausure.io.plume.Retrofit.Entity.ViewListItem;

/**
 * Created by JOJO on 2015/9/11.
 */
public class ViewListAdapter extends RecyclerView.Adapter<ViewListAdapter.ViewHolder>
{
    private Context context;
    private List<ViewListItem> viewList = new ArrayList<>();

    public void addAll(List<ViewListItem> add)
    {
        viewList.addAll(add);
        notifyDataSetChanged();
    }

    public void addAllAfterClear(List<ViewListItem> add)
    {
        viewList.clear();
        addAll(add);
    }

    public List<ViewListItem> getViewPoints()
    {
        return viewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(context == null)
            context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.view_holder,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bindView(viewList != null  && viewList.size() > position ? viewList.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return viewList != null ? viewList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.title)
        public TextView textView;

        @Bind(R.id.image)
        public ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindView(ViewListItem viewListItem)
        {
            if(viewListItem != null)
            {
                textView.setText(viewListItem.getTitle());
                Picasso.with(context).load(viewListItem.getImages().get(0)).into(imageView);
            }
        }
    }
}


