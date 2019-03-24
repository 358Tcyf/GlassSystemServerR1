package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "push_set_table")
public class PushSet extends BaseEntity {


    public static int getTime(int set) {
        switch (set) {
            case 0:
                return 4;
            case 1:
                return 8;
            case 2:
                return 12;
            case 3:
                return 24;
            default:
                return 1;
        }
    }

    public PushSet() {
    }

    public PushSet(boolean pushSwitch, int time, boolean alarmSwitch) {
        this.pushSwitch = pushSwitch;
        this.time = time;
        this.alarmSwitch = alarmSwitch;
    }


    @Column(name = "push_switch")
    private boolean pushSwitch;

    @Column(name = "push_time")
    private int time;

    @OneToMany
    private List<Tag> tags;

    @Column(name = "alarm_switch")
    private boolean alarmSwitch;

    public boolean isPushSwitch() {
        return pushSwitch;
    }

    public void setPushSwitch(boolean pushSwitch) {
        this.pushSwitch = pushSwitch;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isAlarmSwitch() {
        return alarmSwitch;
    }

    public void setAlarmSwitch(boolean alarmSwitch) {
        this.alarmSwitch = alarmSwitch;
    }

    @Override
    public String toString() {
        return "PushSet{" +
                "pushSwitch=" + pushSwitch +
                ", time=" + time +
                ", alarmSwitch=" + alarmSwitch +
                '}';
    }
}
