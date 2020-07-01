package com.example.abc123.my12306.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MyAdapter extends SimpleAdapter {
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    private LayoutInflater inflater;
    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("position", position+"");
        parent.getChildAt(position);
//        if (position==5){
//            convertView= inflater.inflate(R.layout.account_list_item_2,parent,false);
//            convertView.findViewById(R.id.img).setVisibility(View.VISIBLE);
//            return convertView;
//        }
        return super.getView(position, convertView, parent);
    }
}
