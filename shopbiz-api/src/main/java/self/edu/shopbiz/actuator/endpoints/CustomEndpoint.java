package self.edu.shopbiz.actuator.endpoints;

import org.springframework.beans.factory.annotation.Value;
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

    private static final String HOST_NAME = "HOSTNAME";
    private static final String DEFAULT_ENV_INSTANCE_GUID = "LOCAL";

    @Value("${" + HOST_NAME + ":" + DEFAULT_ENV_INSTANCE_GUID + "}")
    private String hostName;

    private Map<String,String> props = new HashMap<>();

    @ReadOperation
    public Map<String,String> envProps() {
        props.put("test", "value");
        props.put("hostName", hostName);
        return props;
    }

    public String retrieveInstanceInfo() {
        return hostName.substring(hostName.length() - 5);
    }


}
