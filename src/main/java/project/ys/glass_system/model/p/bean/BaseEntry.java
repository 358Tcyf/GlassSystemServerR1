package project.ys.glass_system.model.p.bean;

public class BaseEntry<T> {

    public BaseEntry(T xValue, T yValue){
        this.xValue=xValue;
        this.yValue=yValue;
    }

    private T xValue;

    private T yValue;

    public T getxValue() {
        return xValue;
    }

    public void setxValue(T xValue) {
        this.xValue = xValue;
    }

    public T getyValue() {
        return yValue;
    }

    public void setyValue(T yValue) {
        this.yValue = yValue;
    }

    @Override
    public String toString() {
        return "BaseEntry{" +
                "xValue=" + xValue +
                ", yValue=" + yValue +
                '}';
    }
}
