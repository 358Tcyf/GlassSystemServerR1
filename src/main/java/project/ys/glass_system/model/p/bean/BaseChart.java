package project.ys.glass_system.model.p.bean;

import java.util.Arrays;
import java.util.List;

public class BaseChart {


    public BaseChart() {

    }

    public BaseChart(String sub,String title,String desc) {
        this.sub=sub;
        this.title=title;
        this.desc=desc;
    }

    public static int line_chart = 0;
    public static int bar_chart = 1;
    public static int pie_chart = 2;
    public static int ring_chart = 3;

    private String menu;

    private String sub;

    private String title;

    private String desc;

    private int type;

    private boolean only;

    private String label;

    private List<String> labels;

    private String[] xValues;

    private List<BaseEntry> yValues;

    private List<List<BaseEntry>> yListValues;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setType(int type,boolean only) {
        this.type = type;
        this.only = only;
    }
    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String[] getxValues() {
        return xValues;
    }

    public void setxValues(String[] xValues) {
        this.xValues = xValues;
    }

    public List<BaseEntry> getyValues() {
        return yValues;
    }

    public void setyValues(List<BaseEntry> yValues) {
        this.yValues = yValues;
    }

    public List<List<BaseEntry>> getyListValues() {
        return yListValues;
    }

    public void setyListValues(List<List<BaseEntry>> yListValues) {
        this.yListValues = yListValues;
    }

    @Override
    public String toString() {
        return "BaseChart{" +
                "menu='" + menu + '\'' +
                ",\n sub='" + sub + '\'' +
                ",\n title='" + title + '\'' +
                ",\n desc='" + desc + '\'' +
                ",\n type=" + type +
                ",\n only=" + only +
                ",\n label='" + label + '\'' +
                ",\n labels=" + labels +
                ",\n xValues=" + Arrays.toString(xValues) +
                ",\n yValues=" + yValues +
                ",\n yListValues=" + yListValues +
                '}';
    }
}
