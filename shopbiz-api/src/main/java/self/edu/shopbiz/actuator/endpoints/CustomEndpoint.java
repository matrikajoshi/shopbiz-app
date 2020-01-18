package self.edu.shopbiz.actuator.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mpjoshi on 12/23/19.
 */

@Component
@Endpoint(id = "envProps")
public class CustomEndpoint {

    private Map<String,String> props = new HashMap<>();

    @ReadOperation
    public Map<String,String> envProps() {
        props.put("test", "value");
        return props;
    }


}
