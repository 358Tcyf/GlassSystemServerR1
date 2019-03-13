package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Simple
 * @date on 2019/1/3 15:33
 */
@Entity
public class Permission extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
