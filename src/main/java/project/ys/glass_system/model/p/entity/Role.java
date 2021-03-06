package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Role extends BaseEntity {


    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    /*角色身份*/
    @Column(nullable = false, unique = true)
    private String name;

    /*角色描述，可空*/
    @Column
    private String description;

    /*角色权限，可空*/
    @ManyToMany
    private List<Permission> permissionList;

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
