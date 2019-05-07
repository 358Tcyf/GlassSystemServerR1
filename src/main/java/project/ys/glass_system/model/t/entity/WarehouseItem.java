package project.ys.glass_system.model.t.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "gls_warehouse_item")
public class WarehouseItem extends BaseEntity {

    public WarehouseItem() {
    }

    public WarehouseItem(String uuid, LocalDateTime time, Warehouse belong) {
        this.uuid = uuid;
        this.time = time;
        this.belong = belong;
        this.test = false;

    }

    @Column(name = "item_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "is_test")
    private boolean test;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse belong;

    @Column(name = "glass_store_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private List<GlassItem> glasses;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public Warehouse getBelong() {
        return belong;
    }

    public void setBelong(Warehouse belong) {
        this.belong = belong;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public List<GlassItem> getGlasses() {
        return glasses;
    }

    public void setGlasses(List<GlassItem> glasses) {
        this.glasses = glasses;
    }
}
