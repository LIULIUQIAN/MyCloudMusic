package com.example.mycloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.MeGroup;

import java.util.ArrayList;
import java.util.List;

public class MeAdapter extends BaseExpandableListAdapter {

    private final Context context;

    private List<MeGroup> datum = new ArrayList<>();
    private final LayoutInflater inflater;

    public MeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return datum.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datum.get(groupPosition).getDatum().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datum.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datum.get(groupPosition).getDatum().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_title_me, parent, false);
            viewHolder = new GroupViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_topic, parent, false);
            viewHolder = new ChildViewHolder();
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public void setDatum(List<MeGroup> datum) {
        this.datum.clear();
        this.datum.addAll(datum);
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {

    }

    class ChildViewHolder {

    }
}
