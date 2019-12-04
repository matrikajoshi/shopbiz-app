package self.edu.shopbiz.Controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import self.edu.shopbiz.ShopbizApplication;

/**
 * Created by mpjoshi on 11/27/19.
 */

@SpringBootTest(classes = ShopbizApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    @LocalServerPort
    private int port;
}
