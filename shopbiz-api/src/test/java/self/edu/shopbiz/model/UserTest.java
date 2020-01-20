package self.edu.shopbiz.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by mpjoshi on 11/24/19.
 */


public class UserTest {


    @Test
    @Disabled
    public void givenBidirectionRelation_whenUsingJacksonReferenceAnnotation_thenCorrect()
            throws JsonProcessingException {
        User user = new User();
        user.setEmail("ab@c.com");
        user.setPassword("abc");
        Role role = new Role();
        role.setName("ADMIN");
        user.setRoles(Collections.singleton(role));
        role.setUsers(Collections.singletonList(user));
        String result = new ObjectMapper().writeValueAsString(user);
        System.out.println("User: " + result);
        assertThat(result, containsString("ab@c"));
    }

    @Test
    public void givenBidirectionRelation_whenUsingJacksonReferenceAnnotation()
            throws JsonProcessingException {
        User user = new User();
        user.setEmail("ab@c.com");
        user.setPassword("abc");
        Role role = new Role();
        role.setName("ADMIN");
        user.setRoles(Collections.singleton(role));
        role.setUsers(Collections.singletonList(user));
        Permission permission = new Permission();
        permission.setName("ADD_USER");
        role.setPermissions(Collections.singleton(permission));
        permission.setRoles(Arrays.asList(role));
        String result = new ObjectMapper().writeValueAsString(role);
        System.out.println("role: " + result);
        assertThat(result, containsString("ADMIN"));


    }

    @Test
    @Disabled
    public void givenBidirectionRelation_whenUsingJacksonReferenceAnnotation_thenException() throws IOException {

        String jsonUserStr = "{\"email\":\"ab@c.com\", \"password\":\"password\"}";
//        User creds = new ObjectMapper().readValue(jsonUserStr, User.class);
//        assertEquals("ab@c.com", creds.getEmail());
//        assertEquals("password", creds.getPassword());

        Exception exception = assertThrows(JsonMappingException.class, () -> {
            new ObjectMapper().readValue(jsonUserStr, User.class);
        });

        assertTrue(exception.getMessage().contains("Cannot handle managed/back reference"));

    }


    //random test
    @Test
    public void whenNonPublicField_thenReflectionTestUtilsSetField() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1);
        assertTrue(user.getId().equals(1));

    }

}
