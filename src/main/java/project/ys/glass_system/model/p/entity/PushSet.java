package project.ys.glass_system.model.p.entity;

import javax.persistence.*;
import java.util.Date;
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
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        this.start = date.getTime();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        this.end = date.getTime();
    }


    @Column(name = "common_start_date")
    private long start;

    @Column(name = "common_end_date")
    private long end;

    @Column(name = "push_switch")
    private boolean pushSwitch;

    @Column(name = "push_time")
    private int time;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

    @Column(name = "alarm_switch")
    private boolean alarmSwitch;


    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

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
                "start=" + start +
                ", end=" + end +
                ", pushSwitch=" + pushSwitch +
                ", time=" + time +
                ", tags=" + tags +
                ", alarmSwitch=" + alarmSwitch +
                '}';
    }
}
