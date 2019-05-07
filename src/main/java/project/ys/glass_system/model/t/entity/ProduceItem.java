package project.ys.glass_system.model.t.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "gls_produce_table")
public class ProduceItem extends BaseEntity {

    public ProduceItem(){}

    public ProduceItem(LocalDateTime time,String uuid){
        this.time=time;
        this.uuid=uuid;
    }

    @Column(name = "glass_produce_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Column(name = "produce_uuid", nullable = false, unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    private GlassModel model;

    @Column(name = "produce_num", length = 11)
    private int num;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProduceNote belong;

    @Column(name = "wtr_consu", length = 11)
    private float water;

    @Column(name = "elec_consu", length = 11)
    private float electricity;

    @Column(name = "coal_consu", length = 11)
    private float coal;

    @Column(name = "material_consu", length = 11)
    private float material;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public GlassModel getModel() {
        return model;
    }

    public void setModel(GlassModel model) {
        this.model = model;
    }

    public ProduceNote getBelong() {
        return belong;
    }

    public void setBelong(ProduceNote belong) {
        this.belong = belong;
    }

    public void setPlan(ProduceNote belong,GlassModel model,int num){
        this.belong=belong;
        this.model=model;
        this.num=num;
    }

    public void setConsu(float water,float electricity,float coal,float material){
        this.water=water;
        this.electricity=electricity;
        this.coal=coal;
        this.material=material;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getElectricity() {
        return electricity;
    }

    public void setElectricity(float electricity) {
        this.electricity = electricity;
    }

    public float getCoal() {
        return coal;
    }

    public void setCoal(float coal) {
        this.coal = coal;
    }

    public float getMaterial() {
        return material;
    }

    public void setMaterial(float material) {
        this.material = material;
    }

}
