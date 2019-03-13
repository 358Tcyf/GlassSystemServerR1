package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Simple
 * @date on 2019/1/3 15:52
 */
@Entity(name = "push_tags")
public class Tag extends BaseEntity {

    @Column(name = "tag_name", nullable = false, unique = true)
    private String name;

    @Column(name = "tag_path")
    private String description;

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
}
