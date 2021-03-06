package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "gls_warehouse_table")
public class Warehouse extends BaseEntity {

    public Warehouse() {
    }

    public Warehouse(String name, String uuid, Factory own) {
        this.name = name;
        this.uuid = uuid;
        this.own = own;
    }

    @Column(name = "warehouse_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "warehouse_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Factory own;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Factory getOwn() {
        return own;
    }

    public void setOwn(Factory own) {
        this.own = own;
    }
}
