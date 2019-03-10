package project.ys.glass_system.model.p.bean;


public class Push {


    public Push() {

    }

    public Push(long createTime) {
        this.createTime = createTime;
        this.haveRead = false;
    }

    public Push(String content) {
        this.content = content;
    }

    private String title;

    private String content;

    private String receiver;

    private String pushUuid;

    private long createTime;

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
        return "Push{" +
                "title='" + title + '\'' +
                ",\n content='" + content + '\'' +
                ",\n  receiver='" + receiver + '\'' +
                ",\n  pushUuid='" + pushUuid + '\'' +
                ",\n  createTime=" + createTime +
                ",\n  haveRead=" + haveRead +
                '}';
    }
}
