package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "gls_factory_table")
public class Factory extends BaseEntity{

    public Factory(){}
    public Factory( String name,String uuid,String province,String city){
        this.name=name;
        this.uuid=uuid;
        this.province=province;
        this.city=city;
    }

    @Column(name = "factory_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "factory_name", nullable = false)
    private String name;

    @Column(name = "factory_province")
    private String province;

    @Column(name = "factory_city")
    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
