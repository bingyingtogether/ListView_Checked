package com.xhkj.listview_checked.adpapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhkj.listview_checked.R;
import com.xhkj.listview_checked.model.FoodModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xhkj_wjb on 2017/4/11.
 */

public class SingleLevelAdapter extends BaseAdapter {
    private Context context;
    private List<FoodModel.Range.Sub> list;
    private LayoutInflater inflater;
    public static Map<Integer,Boolean> isSelected;

    public SingleLevelAdapter(Context context, List<FoodModel.Range.Sub> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        isSelected = new HashMap<>();
        for (int i = 0; i<list.size();i++){
            isSelected.put(i,false);
        }
    }
    //清楚   把所有状态清楚为未选中状态  多级单选
    public  void clear(){
        for (int i = 0; i<list.size();i++){
            isSelected.put(i,false);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder =null;
        if (view==null){
            view = inflater.inflate(R.layout.child_layout,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.title  = (TextView) view.findViewById(R.id.child_text);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.my_CheckBox);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(list.get(position).getName());
        viewHolder.checkBox.setChecked(isSelected.get(position));
        return view;
    }

    public class ViewHolder{
        public TextView title;
        public CheckBox checkBox;
    }
}
