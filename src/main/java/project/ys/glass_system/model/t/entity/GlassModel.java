package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import static project.ys.glass_system.util.UuidUtil.getUUID32;

@Entity(name = "gls_model_table")
public class GlassModel extends BaseEntity {

    public GlassModel() {
    }

    public GlassModel(String name) {
        this.name = name;
        this.uuid = getUUID32();
    }

    @Column(name = "model_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "model_name", nullable = false)
    private String name;

    @Column(name = "model_desc")
    private String desc;

    @Column(name = "model_length")
    private int length;

    @Column(name = "model_width")
    private int width;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

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

    @Override
    public String toString() {
        return "GlassModel{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", length=" + length +
                ", width=" + width +
                '}';
    }
}
