package project.ys.glass_system.model.p.bean;

import java.util.Arrays;
import java.util.List;

public class BaseChart {

    public static int line_chart = 0;
    public static int bar_chart = 1;
    public static int pie_chart = 2;
    public static int ring_chart = 3;

    private String title;

    private String subtitle;

    private int chart_type;

    private boolean only;

    private String label;

    private List<String> labels;

    private String[] xValues;

    private List<BaseEntry> yValues;

    private List<List<BaseEntry>> yListValues;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getChart_type() {
        return chart_type;
    }

    public void setChart_type(int chart_type) {
        this.chart_type = chart_type;
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
        String result = "BaseChart{" +
                "title='" + title + '\'' +
                ",\n subtitle='" + subtitle + '\'' +
                ",\n chart_type=" + chart_type +
                ",\n only=" + only;
        if (only)
            result += ",\n" + " label='" + label + '\'' + ",\n xValues=" + Arrays.toString(xValues) + ",\n" + " yValues=" + yValues + '}';
        else
            result += ",\n" + " labels=" + labels + ",\n xValues=" + Arrays.toString(xValues) + ",\n" + " yListValues=" + yListValues + '}';
        return result;
    }
}
