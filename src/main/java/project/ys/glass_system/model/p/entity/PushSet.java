package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
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
        this(true, true, true, true, pushSwitch, time, alarmSwitch);
    }

    public PushSet(boolean commonSwitch, boolean sound, boolean vibrate, boolean flags, boolean pushSwitch, int time, boolean alarmSwitch) {
        this.commonSwitch = commonSwitch;
        this.sound = sound;
        this.vibrate = vibrate;
        this.flags = flags;
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        this.start = date.getTime();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        this.end = date.getTime();
        this.pushSwitch = pushSwitch;
        this.time = time;
        this.alarmSwitch = alarmSwitch;
    }


    @Column(name = "common_switch")
    private boolean commonSwitch;

    @Column(name = "sound_swithc")
    private boolean sound;

    @Column(name = "vibrate_switch")
    private boolean vibrate;

    @Column(name = "flags_switch")
    private boolean flags;


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

    @Column(name = "smart_subscription_switch")
    private boolean smartSub;


    @ManyToMany
    private List<AlarmTag> alarmTags;

    @Column(name = "alarm_switch")
    private boolean alarmSwitch;

    public boolean isCommonSwitch() {
        return commonSwitch;
    }

    public void setCommonSwitch(boolean commonSwitch) {
        this.commonSwitch = commonSwitch;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isFlags() {
        return flags;
    }

    public void setFlags(boolean flags) {
        this.flags = flags;
    }

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

    public List<AlarmTag> getAlarmTags() {
        return alarmTags;
    }

    public void setAlarmTags(List<AlarmTag> alarmTags) {
        this.alarmTags = alarmTags;
    }

    public boolean isSmartSub() {
        return smartSub;
    }

    public void setSmartSub(boolean smartSub) {
        this.smartSub = smartSub;
    }

    @Override
    public String toString() {
        return "PushSet{" +
                "commonSwitch=" + commonSwitch +
                ", sound=" + sound +
                ", vibrate=" + vibrate +
                ", flags=" + flags +
                ", start=" + start +
                ", end=" + end +
                ", pushSwitch=" + pushSwitch +
                ", time=" + time +
                ", tags=" + tags +
                ", smartSub=" + smartSub +
                ", alarmTags=" + alarmTags +
                ", alarmSwitch=" + alarmSwitch +
                '}';
    }
}
