package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "gls_table")
public class GlassItem extends BaseEntity {

    public GlassItem() {
    }

    public GlassItem(ProduceItem produce, GlassModel model, String uuid) {
        this.produce = produce;
        this.model = model;
        this.uuid = uuid;
    }

    @Column(name = "glass_uuid", nullable = false, unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    private GlassModel model;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestRank rank;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProduceItem produce;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestItem test;

    @ManyToOne(fetch = FetchType.EAGER)
    private WarehouseItem store;

    public WarehouseItem getStore() {
        return store;
    }

    public void setStore(WarehouseItem store) {
        this.store = store;
    }

    public TestItem getTest() {
        return test;
    }

    public void setTest(TestItem test) {
        this.test = test;
    }

    public TestRank getRank() {
        return rank;
    }

    public void setRank(TestRank rank) {
        this.rank = rank;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GlassModel getModel() {
        return model;
    }

    public void setModel(GlassModel model) {
        this.model = model;
    }

    public ProduceItem getProduce() {
        return produce;
    }

    public void setProduce(ProduceItem produce) {
        this.produce = produce;
    }
}
