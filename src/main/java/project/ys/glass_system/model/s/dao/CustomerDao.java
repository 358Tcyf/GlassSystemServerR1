package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.Customers;

public interface CustomerDao extends JpaRepository<Customers, Integer> {

    Customers findCustomersByPhoneOrEmail(String phone, String email);

    Customers findCustomersByPhone(String phone);

    Customers findCustomersByEmail(String email);

}
