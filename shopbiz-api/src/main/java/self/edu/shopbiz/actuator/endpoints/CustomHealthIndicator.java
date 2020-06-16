package self.edu.shopbiz.actuator.endpoints;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

//import org.springframework.web.client.RestTemplate;

/**
 * Created by mpjoshi on 12/23/19.
 */

@Component
public class CustomHealthIndicator implements HealthIndicator {

    //TO DO - to be added later
    String promoServiceUrl = "http://example.com";

//    @Autowired
//    RestTemplate restTemplate;

    @Override
    public Health health() {

//        try {
//            ResponseEntity<String> responseEntity = restTemplate.getForEntity(promoServiceUrl, String.class);
//            if(responseEntity.getStatusCode().is5xxServerError()) {
//                return Health.down().withDetail("PromoService error: ", responseEntity.getStatusCode()).build();
//            }
//        } catch(RestClientException ex) {
//            return Health.down().withDetail("PromoService error: ", ex.getMessage()).build();
//        }

        return Health.up().build();
    }
}