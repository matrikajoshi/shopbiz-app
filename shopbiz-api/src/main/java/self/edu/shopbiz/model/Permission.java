package self.edu.shopbiz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import self.edu.shopbiz.util.CustomListDeserializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by mpjoshi on 10/18/19.
 */


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
public class Permission {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    //Inverse relationship
    //Bi-directional ManyToMany relationship
    //With mappedBy attribute column will not be created in the table
    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference(value="permissions") // for json infinite recursion problem
    private List<Role> roles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
