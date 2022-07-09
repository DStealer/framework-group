package cn.com.coderd.framework.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网关消息编码转换信息配置
 */
@Data
@ConfigurationProperties(prefix = "gateway.tenant")
public class GatewayTenantProperties implements Serializable {
    private transient Map<String, KeySpec> jsonmSpecs = new HashMap<>();
    private transient Map<String, KeySpec> jsonxSpecs = new HashMap<>();
    private List<String> excludePatterns = new ArrayList<>(0);
}