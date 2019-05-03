package project.ys.glass_system.model.p.bean;

public class EChartPieEntry {

    public EChartPieEntry(String name, Object value){
        this.name=name;
        this.value= Float.valueOf(String.valueOf(value));
    }
    public EChartPieEntry(String name, float value){
        this.name=name;
        this.value=value;
    }
    private String name;
    private float value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
