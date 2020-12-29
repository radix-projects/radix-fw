package application.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "cache")
@Data
public class CacheConfigurationProperties {

    private String host;
    private Integer port;
    private String password;
    private long timeoutSeconds;
    private Map<String, Long> cacheExpirations = new HashMap<>();
}
