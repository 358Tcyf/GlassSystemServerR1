package project.ys.glass_system.model.p.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends BaseEntity {

    public User() {
    }

    public User(String no, String password) {
        this.no = no;
        this.password = password;
    }

    public User(String no, String password, String name, String phone, String email) {
        this.no = no;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


    /*可公开*/
    @Column(nullable = false, unique = true)
    private String no;

    /*不可公开*/
    @Column(nullable = false)
    private String password;

    /*可公开*/
    @Column
    private String name;

    /*可公开*/
    @ManyToOne
    private Role role;

    /*可公开*/
    @Column
    private String phone;

    /*可公开*/
    @Column
    private String email;

    /*不可公开*/
    @Column
    private String tags;

    /*可公开*/
    @ManyToMany
    private List<File> files;

    /*可公开*/
    @OneToOne
    private File pic;

    /*可公开*/
    @OneToOne
    private PushSet pushSet;

    /*不必公开*/
    @ManyToMany
    private List<Tag> care;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public File getPic() {
        return pic;
    }

    public void setPic(File pic) {
        this.pic = pic;
    }

    public PushSet getPushSet() {
        return pushSet;
    }

    public void setPushSet(PushSet pushSet) {
        this.pushSet = pushSet;
    }

    public List<Tag> getCare() {
        return care;
    }

    public void setCare(List<Tag> care) {
        this.care = care;
    }
}
