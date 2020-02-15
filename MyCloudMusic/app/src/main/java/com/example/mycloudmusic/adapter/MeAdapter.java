package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.MeGroup;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.event.CreateSheetClickEvent;
import com.example.mycloudmusic.util.ImageUtil;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.bindData(datum.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_topic, parent, false);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        viewHolder.bindData(datum.get(groupPosition).getDatum().get(childPosition));

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

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.iv_more)
        ImageView iv_more;

        @OnClick(R.id.iv_more)
        public void onMoreClick(){

            EventBus.getDefault().post(new CreateSheetClickEvent());
        }

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(MeGroup meGroup) {

            tv_title.setText(meGroup.getTitle());

            if (meGroup.isMore()){
                iv_more.setVisibility(View.VISIBLE);
            }else {
                iv_more.setVisibility(View.GONE);
            }

        }
    }

    class ChildViewHolder {

        @BindView(R.id.iv_banner)
        ImageView iv_banner;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_info)
        TextView tv_info;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(Sheet sheet) {

            ImageUtil.showAvatar((Activity) context,iv_banner,sheet.getBanner());
            tv_title.setText(sheet.getTitle());
            tv_info.setText(context.getResources().getString(R.string.song_count,sheet.getSongsCount()));

        }
    }
}
