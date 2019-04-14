package project.ys.glass_system.model.p.entity;

import javax.persistence.Entity;

@Entity
public class LongString extends BaseEntity {

    private String uuid;
    private int sequence;
    private String str;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
