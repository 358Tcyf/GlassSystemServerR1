package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Simple
 * @date on 2019/1/3 15:52
 */
@Entity(name = "push_tags")
public class Tag extends BaseEntity {

    public static String DailyProduceCountList = "生产量";
    public static String DailyCountOfModel = "生产型号统计";
    public static String DailyProduceQualityList = "生产质量";
    public static String DailyConsume = "生产能耗";

    public Tag() {
    }

    public Tag(String name) {
        this(name, "统计玻璃" + name);
    }

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }


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

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
