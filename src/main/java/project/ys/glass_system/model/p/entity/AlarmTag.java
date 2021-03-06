package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "alarm_tags")
public class AlarmTag extends BaseEntity {

    public AlarmTag() {
    }


    public AlarmTag(String content) {
        this(content, 0, 0);
    }

    public AlarmTag(String content, float min, float max) {
        this.content = content;
    }

    @Column(name = "tag_content")
    private String content;

    @Column(name = "min_value")
    private float min;

    @Column(name = "max_value")
    private float max;

    @ManyToOne(fetch = FetchType.EAGER)
    private PushSet set;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public PushSet getSet() {
        return set;
    }

    public void setSet(PushSet set) {
        this.set = set;
    }
}
