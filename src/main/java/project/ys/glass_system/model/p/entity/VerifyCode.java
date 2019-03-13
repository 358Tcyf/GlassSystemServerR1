package project.ys.glass_system.model.p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VerifyCode extends BaseEntity {

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
