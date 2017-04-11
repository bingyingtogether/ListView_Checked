package com.xhkj.listview_checked.model;

import java.util.List;

/**
 * Created by xhkj_wjb on 2017/4/11.
 */

public class FoodModel {
    public List<Range> getRange() {
        return range;
    }

    public void setRange(List<Range> range) {
        this.range = range;
    }

    private List<Range> range;

    public static class Range {
        private String id;
        private String name;
        private String pid;

        public boolean isParent_select() {
            return parent_select;
        }

        public void setParent_select(boolean parent_select) {
            this.parent_select = parent_select;
        }

        private boolean parent_select;

        public List<Sub> getSub() {
            return sub;
        }

        public void setSub(List<Sub> sub) {
            this.sub = sub;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        private List<Sub> sub;

        public static class Sub {
            private String id;
            private String name;

            public boolean isChildSelect() {
                return childSelect;
            }

            public void setChildSelect(boolean childSelect) {
                this.childSelect = childSelect;
            }

            private boolean childSelect;

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String pid;
        }

    }
}
