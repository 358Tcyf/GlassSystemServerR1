package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "user_file")
public class File extends BaseEntity {

    private String uuid;

    private String name;

    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
