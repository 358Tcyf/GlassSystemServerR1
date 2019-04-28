package project.ys.glass_system.model.p.entity;

import javax.persistence.*;

@Entity(name = "user_action_record")
public class Record extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    User user;

    /*
    * 01->android登录
    * 02->web登录
    * 11->备份推送
    * 12->下载推送
    * */
    @Column(name = "user_action", nullable = false)
    private int action;

    @Column(name = "action_time", nullable = false)
    private long time;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
