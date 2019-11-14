package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.ShoppingList;
import self.edu.shopbiz.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 9/29/19.
 */

@Repository("shoppingListRepository")
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer>{

    Optional<ShoppingList> findByUser(User user);

    @Query(value = "SELECT sl from ShoppingList sl where sl.user.id = :customerId")
    public List<ShoppingList> findByCustomerId(@Param("customerId") Integer customerId);

    @Query(value = "SELECT sl from ShoppingList sl where sl.name = :name AND sl.user.id = :customerId")
    public Optional<ShoppingList> findByCustomerIdAndName(
            @Param("name") String name,
            @Param("customerId") Integer customerId);


}
