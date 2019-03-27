package project.ys.glass_system.model.p.entity;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "alarm_table")
public class Alarm extends BaseEntity{

    public Alarm() {

    }

    public Alarm(long createTime) {
        this.createTime = createTime;
        this.haveRead = false;
    }

    public Alarm(String content) {
        this.content = content;
    }

    @Column(name = "alarm_title", nullable = false)
    private String title;

    @Column(name = "alarm_content")
    private String content;

    @Column(name = "receiver_uuid")
    private String receiver;

    @Column(name = "alarm_uuid", nullable = false, unique = true)
    private String alarmUuid;


    @Column(name = "push_createTime", nullable = false)
    private long createTime;

    @Column(name = "receiver_haveRead")
    private boolean haveRead;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAlarmUuid() {
        return alarmUuid;
    }

    public void setAlarmUuid(String alarmUuid) {
        this.alarmUuid = alarmUuid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead) {
        this.haveRead = haveRead;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", receiver='" + receiver + '\'' +
                ", alarmUuid='" + alarmUuid + '\'' +
                ", createTime=" + createTime +
                ", haveRead=" + haveRead +
                '}';
    }
}
