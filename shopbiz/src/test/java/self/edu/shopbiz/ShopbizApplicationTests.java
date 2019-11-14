package self.edu.shopbiz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import self.edu.shopbiz.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopbizApplication.class)
public class ShopbizApplicationTests {


//	//@LocalServerPort
	private int port = 8080;
//
	//random test
	@Test
	@DirtiesContext
	public void whenNonPublicField_thenReflectionTestUtilsSetField() {
		User user = new User();
		ReflectionTestUtils.setField(user, "id", 1);
		assertTrue(user.getId().equals(1));

	}


}
