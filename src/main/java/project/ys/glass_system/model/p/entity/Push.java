package project.ys.glass_system.model.p.entity;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "push_table")
public class Push extends BaseEntity{

    public Push() {

    }

    public Push(long createTime) {
        this.createTime = createTime;
        this.haveRead = false;
    }

    public Push(String content) {
        this.content = content;
    }

    @Column(name = "push_title", nullable = false)
    private String title;

    @Column(name = "push_content")
    private String content;

    @Column(name = "receiver_uuid")
    private String receiver;

    @Column(name = "push_uuid", nullable = false, unique = true)
    private String pushUuid;

    @Column(name = "receiver_default_see")
    private String defaultSubMenu;

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

    public String getPushUuid() {
        return pushUuid;
    }

    public void setPushUuid(String pushUuid) {
        this.pushUuid = pushUuid;
    }

    public String getDefaultSubMenu() {
        return defaultSubMenu;
    }

    public void setDefaultSubMenu(String defaultSubMenu) {
        this.defaultSubMenu = defaultSubMenu;
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


}
