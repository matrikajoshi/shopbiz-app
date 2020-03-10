package self.edu.shopbiz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import self.edu.shopbiz.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getByName(String roleCustomer);
}
