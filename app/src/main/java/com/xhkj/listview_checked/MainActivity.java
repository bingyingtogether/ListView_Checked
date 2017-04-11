package com.xhkj.listview_checked;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xhkj.listview_checked.adpapter.MultilevelAdapter;
import com.xhkj.listview_checked.adpapter.SingleLevelAdapter;
import com.xhkj.listview_checked.model.FoodModel;
import com.xhkj.listview_checked.utils.UtilTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button single_radio, single_stage_selection, multi_choice, multilevel_radio;
    private String multilevelData;
    //多级
    private List<FoodModel.Range> rangeList;//一级
    private List<FoodModel.Range.Sub> rangeList_selected;//二级多选被选中的集合
    private MyDialog multilevelDialog;//多级多选Dialog
    private MyDialog multi_choiceDialog;//多级单选Dialog
    //单级
    private List<FoodModel.Range.Sub> singleLevelList;//二级
    private List<String> singleLevelList_selected;//单级多选 选中的集合
    private MyDialog single_radioDialog;//单级单选Dialog
    private MyDialog single_stage_selectionDialog;//单级多选Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getData();
        setData();
    }

    private void initViews() {
        single_radio = (Button) findViewById(R.id.single_radio);
        single_stage_selection = (Button) findViewById(R.id.single_stage_selection);
        multi_choice = (Button) findViewById(R.id.multi_choice);
        multilevel_radio = (Button) findViewById(R.id.multilevel_radio);
        //多级
        rangeList = new ArrayList<>();
        rangeList_selected = new ArrayList<>();
        //单级
        singleLevelList = new ArrayList<>();
        singleLevelList_selected = new ArrayList<>();
    }

    //获取本地数据
    public void getData() {
        try {
            InputStreamReader inputReader = new InputStreamReader(getAssets().open("duoji.json"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                multilevelData += line;
            }
            multilevelData = multilevelData.trim().replace(" ", "").replace("null", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        Log.e("multilevelData", multilevelData);
        Gson gson = new Gson();
        FoodModel model = gson.fromJson(multilevelData, FoodModel.class);
        rangeList = model.getRange();
        singleLevelList = rangeList.get(0).getSub();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_radio:
                showSingleRadio(single_radio);
                break;
            case R.id.single_stage_selection:
                showSingle_stage_selection(single_stage_selection);
                break;
            case R.id.multi_choice:
                showMulti_choice(multi_choice);
                break;
            case R.id.multilevel_radio:
                showMultilevel_radio(multilevel_radio);
                break;
        }
    }

    /**
     * @param choice 单级单选
     */
    public void showSingleRadio(final Button choice) {
        View rangeView = LayoutInflater.from(this).inflate(R.layout.single_select, null);
        Button cancel, sure;
        cancel = (Button) rangeView.findViewById(R.id.cancel);
        sure = (Button) rangeView.findViewById(R.id.sure);
        final TextView choice_areas = (TextView) rangeView.findViewById(R.id.choice_areas);
        ListView listView = (ListView) rangeView.findViewById(R.id.listView);
        final SingleLevelAdapter adapter = new SingleLevelAdapter(this, singleLevelList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SingleLevelAdapter.ViewHolder viewHolder = (SingleLevelAdapter.ViewHolder) view.getTag();
                viewHolder.checkBox.toggle();
                adapter.clear();
                SingleLevelAdapter.isSelected.put(position, viewHolder.checkBox.isChecked());
                choice_areas.setText(singleLevelList.get(position).getName());
            }
        });
        int width = UtilTools.getScreenWidth(this) * 6 / 7;
        int height = UtilTools.getScreenHeight(this) - UtilTools.getStatusBarHeight(this);//屏幕高度-状态栏和标题栏高度
        if (single_radioDialog == null) {
            single_radioDialog = new MyDialog(this, R.style.MyDialog, rangeView);
            single_radioDialog.show();
            WindowManager.LayoutParams lp = single_radioDialog.getWindow().getAttributes();
            lp.width = width;
            lp.height = height;
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            single_radioDialog.getWindow().setAttributes(lp);
        } else {
            adapter.notifyDataSetChanged();
            single_radioDialog.show();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single_radioDialog.hide();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single_radioDialog.hide();
                String result = choice_areas.getText().toString();
                if (result.equals("")) {
                    choice.setText("单级单选");
                } else {
                    choice.setText(result);
                }
            }
        });
    }

    /**
     * @param choice 单级多选
     */
    public void showSingle_stage_selection(final Button choice) {
        View rangeView = LayoutInflater.from(this).inflate(R.layout.single_select, null);
        Button cancel, sure;
        cancel = (Button) rangeView.findViewById(R.id.cancel);
        sure = (Button) rangeView.findViewById(R.id.sure);
        final TextView choice_areas = (TextView) rangeView.findViewById(R.id.choice_areas);
        ListView listView = (ListView) rangeView.findViewById(R.id.listView);
        SingleLevelAdapter adapter = new SingleLevelAdapter(this, singleLevelList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SingleLevelAdapter.ViewHolder viewHolder = (SingleLevelAdapter.ViewHolder) view.getTag();
                viewHolder.checkBox.toggle();
                SingleLevelAdapter.isSelected.put(position, viewHolder.checkBox.isChecked());
                if (viewHolder.checkBox.isChecked()) {
                    singleLevelList_selected.add(singleLevelList.get(position).getName());
                } else {
                    singleLevelList_selected.remove(singleLevelList.get(position).getName());
                }
                if (singleLevelList_selected.size() == 0) {
                    choice_areas.setText("");
                } else {
                    for (int i = 0; i < singleLevelList_selected.size(); i++) {
                        if (i == 0) {
                            choice_areas.setText(singleLevelList_selected.get(i));
                        } else {
                            choice_areas.append("、" + singleLevelList_selected.get(i));
                        }
                    }
                }
            }
        });
        int width = UtilTools.getScreenWidth(this) * 6 / 7;
        int height = UtilTools.getScreenHeight(this) - UtilTools.getStatusBarHeight(this);//屏幕高度-状态栏和标题栏高度
        if (single_stage_selectionDialog == null) {
            single_stage_selectionDialog = new MyDialog(this, R.style.MyDialog, rangeView);
            single_stage_selectionDialog.show();
            WindowManager.LayoutParams lp = single_stage_selectionDialog.getWindow().getAttributes();
            lp.width = width;
            lp.height = height;
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            single_stage_selectionDialog.getWindow().setAttributes(lp);
        } else {
            adapter.notifyDataSetChanged();
            single_stage_selectionDialog.show();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single_stage_selectionDialog.hide();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single_stage_selectionDialog.hide();
                String result = choice_areas.getText().toString();
                if (result.equals("")) {
                    choice.setText("单级多选");
                } else {
                    choice.setText(result);
                }
            }
        });
    }

    /**
     * @param choice 多级单选
     */
    public void showMulti_choice(final Button choice) {
        View rangeView = LayoutInflater.from(this).inflate(R.layout.multi_select, null);
        Button cancel, sure;
        cancel = (Button) rangeView.findViewById(R.id.cancel);
        sure = (Button) rangeView.findViewById(R.id.sure);
        final TextView choice_areas = (TextView) rangeView.findViewById(R.id.choice_areas);
        ExpandableListView exListView = (ExpandableListView) rangeView.findViewById(R.id.exListView);
        exListView.setGroupIndicator(null);//默认图标不显示
        final MultilevelAdapter adapter = new MultilevelAdapter(this, rangeList);
        exListView.setAdapter(adapter);
        // 设置一级item点击的监听器
        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (rangeList.get(groupPosition).isParent_select() == false) {
                    rangeList.get(groupPosition).setParent_select(true);
                } else {
                    rangeList.get(groupPosition).setParent_select(false);
                }
                return false;
            }
        });
        // 设置二级item点击的监听器
        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                MultilevelAdapter.ChilViewHolder viewHolder = (MultilevelAdapter.ChilViewHolder) v.getTag();
                viewHolder.checkBox.toggle();
                adapter.clear();
                MultilevelAdapter.childIsSelector.get(groupPosition).put(childPosition, viewHolder.checkBox.isChecked());
                if (viewHolder.checkBox.isChecked()) {
                    choice_areas.setText(rangeList.get(groupPosition).getSub().get(childPosition).getName());
                } else {
                    choice_areas.setText("");
                }
                return false;
            }
        });
        int width = UtilTools.getScreenWidth(this) * 6 / 7;
        int height = UtilTools.getScreenHeight(this) - UtilTools.getStatusBarHeight(this);//屏幕高度-状态栏和标题栏高度
        if (multi_choiceDialog == null) {
            multi_choiceDialog = new MyDialog(this, R.style.MyDialog, rangeView);
            multi_choiceDialog.show();
            WindowManager.LayoutParams lp = multi_choiceDialog.getWindow().getAttributes();
            lp.width = width;
            lp.height = height;
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            multi_choiceDialog.getWindow().setAttributes(lp);
        } else {
            adapter.notifyDataSetChanged();
            multi_choiceDialog.show();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multi_choiceDialog.hide();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multi_choiceDialog.hide();
                String result = choice_areas.getText().toString();
                if (result.equals("")) {
                    choice.setText("多级单选");
                } else {
                    choice.setText(result);
                }

            }
        });
    }

    /**
     * @param choice 多级多选
     */
    public void showMultilevel_radio(final Button choice) {
        View rangeView = LayoutInflater.from(this).inflate(R.layout.multi_select, null);
        Button cancel, sure;
        cancel = (Button) rangeView.findViewById(R.id.cancel);
        sure = (Button) rangeView.findViewById(R.id.sure);
        final TextView choice_areas = (TextView) rangeView.findViewById(R.id.choice_areas);
        ExpandableListView exListView = (ExpandableListView) rangeView.findViewById(R.id.exListView);
        exListView.setGroupIndicator(null);//默认图标不显示
        MultilevelAdapter adapter = new MultilevelAdapter(this, rangeList);
        exListView.setAdapter(adapter);
        // 设置一级item点击的监听器
        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (rangeList.get(groupPosition).isParent_select() == false) {
                    rangeList.get(groupPosition).setParent_select(true);
                } else {
                    rangeList.get(groupPosition).setParent_select(false);
                }
                return false;
            }
        });
        // 设置二级item点击的监听器
        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                MultilevelAdapter.ChilViewHolder viewHolder = (MultilevelAdapter.ChilViewHolder) v.getTag();
                viewHolder.checkBox.toggle();
                MultilevelAdapter.childIsSelector.get(groupPosition).put(childPosition, viewHolder.checkBox.isChecked());

                if (viewHolder.checkBox.isChecked()) {
                    rangeList_selected.add(rangeList.get(groupPosition).getSub().get(childPosition));
                } else {
                    rangeList_selected.remove(rangeList.get(groupPosition).getSub().get(childPosition));
                }
                if (rangeList_selected.size() == 0) {
                    choice_areas.setText("");
                } else {
                    for (int i = 0; i < rangeList_selected.size(); i++) {
                        if (i == 0) {
                            choice_areas.setText(rangeList_selected.get(i).getName());
                        } else {
                            choice_areas.append("、" + rangeList_selected.get(i).getName());
                        }
                    }
                }
                return false;
            }
        });
        int width = UtilTools.getScreenWidth(this) * 6 / 7;
        int height = UtilTools.getScreenHeight(this) - UtilTools.getStatusBarHeight(this);//屏幕高度-状态栏和标题栏高度
        if (multilevelDialog == null) {
            multilevelDialog = new MyDialog(this, R.style.MyDialog, rangeView);
            multilevelDialog.show();
            WindowManager.LayoutParams lp = multilevelDialog.getWindow().getAttributes();
            lp.width = width;
            lp.height = height;
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            multilevelDialog.getWindow().setAttributes(lp);
        } else {
            adapter.notifyDataSetChanged();
            multilevelDialog.show();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multilevelDialog.hide();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multilevelDialog.hide();
                String result = choice_areas.getText().toString();
                if (result.equals("")) {
                    choice.setText("多级多选");
                } else {
                    choice.setText(result);
                }

            }
        });
    }
}
