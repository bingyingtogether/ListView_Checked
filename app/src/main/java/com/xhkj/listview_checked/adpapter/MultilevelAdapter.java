package com.xhkj.listview_checked.adpapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhkj.listview_checked.R;
import com.xhkj.listview_checked.model.FoodModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xhkj_wjb on 2017/4/11.
 * 多级多选   多级单选
 */

public class MultilevelAdapter extends BaseExpandableListAdapter {
    // 这个数组是用来存储一级item的点击次数的，根据点击次数设置一级标签的选中、未选中状态
    public static Map<Integer, Boolean> isSelector;
    //储存二级目录选中未选中状态
    public static Map<Integer, Map<Integer, Boolean>> childIsSelector;
    private Context context;
    private List<FoodModel.Range> list;
    private LayoutInflater inflater;

    public MultilevelAdapter(Context context, List<FoodModel.Range> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelector = new HashMap<>();
        childIsSelector = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            isSelector.put(i, false);
            Map<Integer, Boolean> map = new HashMap<>();
            for (int j = 0; j < list.get(i).getSub().size(); j++) {
                map.put(j, false);
            }
            childIsSelector.put(i, map);
        }
    }
    //清楚   把所有状态清楚为未选中状态  多级单选
    public  void clear(){
        for (int i = 0; i < list.size(); i++) {
            Map<Integer, Boolean> map = new HashMap<>();
            for (int j = 0; j < list.get(i).getSub().size(); j++) {
                map.put(j, false);
            }
            childIsSelector.put(i, map);
        }
        notifyDataSetChanged();
    }
    /**
     * 获取组的个数
     * 返回值：  组的个数
     */
    @Override
    public int getGroupCount() {
        return list.size();
    }
    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition：组位置（决定返回哪个组的子元素个数）
     * @return：指定组的子元素个数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getSub().size();
    }
    /**
     * 获取指定组中的数据
     *
     * @param groupPosition：组位置
     * @return：返回组中的数据，也就是该组中的子元素数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }
    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition：组的位置
     * @param childPosition：组中子元素的位置
     * @return：返回指定子元素数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getSub().get(childPosition);
    }
    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition：组位置
     * @return：返回组相关ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition：
     * @param childPosition：
     * @return：返回具体组里某个元素的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return：返回一个Boolean类型的值，如果为TRUE，意味着相同的ID永远引用相同的对象
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     * 获取显示指定组的视图对象。这个方法仅返回关于组的视图对象，
     * 要想获取子元素的视图对象，就需要调用getChildView(int, int, boolean, View, ViewGroup)。
     *
     * @param groupPosition：组位置（决定返回哪个视图）
     * @param isExpanded：该组是展开状态还是伸缩状态
     * @param convertView：该组是展开状态还是伸缩状态   注意：在使用前你应该检查一下这个视图对象是否非空并且这个对象的类型是否合适。
     *                                    由此引伸出，如果该对象不能被转换并显示正确的数据，
     *                                    这个方法就会调用getGroupView(int, boolean, View, ViewGroup)来创建一个视图(View)对象。
     * @param parent：返回的视图对象始终依附于的视图组。
     * @return：返回指定组的视图对象
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.parent_layout,parent,false);
        // 新建一个TextView对象，用来显示一级标签上的标题信息
        TextView group_title = (TextView) convertView
                .findViewById(R.id.group_title);
        // 设置标题上的文本信息
        group_title.setText(list.get(groupPosition).getName());
        return convertView;
    }
    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition：组位置（该组内部含有子元素）
     * @param childPosition：                 子元素位置（决定返回哪个视图）
     * @param isLastChild：子元素是否处于组中的最后一个
     * @param convertView：                   重用已有的视图
     * @param parent：返回的视图(View)对象始终依附于的视图组。
     * @return： 指定位置上的子元素返回的视图对象
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChilViewHolder chilViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.child_layout, null);
            chilViewHolder = new ChilViewHolder();
            chilViewHolder.child_text = (TextView) convertView
                    .findViewById(R.id.child_text);
            chilViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.my_CheckBox);
            convertView.setTag(chilViewHolder);
        } else {

            chilViewHolder = (ChilViewHolder) convertView.getTag();
        }
        chilViewHolder.child_text.setText(list.get(groupPosition).getSub().get(childPosition).getName());
        chilViewHolder.checkBox.setChecked(childIsSelector.get(groupPosition).get(childPosition));
        return convertView;
    }
    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition：组位置
     * @param childPosition：子元素位置
     * @return：是否选中子元素
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public class ChilViewHolder {
        public TextView child_text;
        public CheckBox checkBox;
    }
}
