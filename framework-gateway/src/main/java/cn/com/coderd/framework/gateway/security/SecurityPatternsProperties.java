package cn.com.coderd.framework.gateway.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties("gateway.security")
public class SecurityPatternsProperties implements Serializable {
    private boolean showDetail;
    private String urlPattern;
    private String htmlPattern;
    private String jsPattern;
    private String sqlPattern;
    private List<SpecialPattern> specialPatterns;

    /**
     * 转换并检查配置
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, String>> convertSpecialPatterns() {
        if (this.specialPatterns == null || this.specialPatterns.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Map<String, String>> patternMap = new HashMap<>();
        for (SpecialPattern specialPattern : this.specialPatterns) {
            if (patternMap.containsKey(specialPattern.getUrl())) {
                throw new RuntimeException(String.format("url:[%s]配置重复", specialPattern.getUrl()));
            }
            List<FiledPattern> filedPatterns = specialPattern.getPatterns();
            if (filedPatterns == null || filedPatterns.isEmpty()) {
                continue;
            }
            Map<String, String> filedMap = new HashMap<>();
            for (FiledPattern filedPattern : filedPatterns) {
                if (filedMap.containsKey(filedPattern.getFiled())) {
                    throw new RuntimeException(String.format("url:[%s]field:[%s]配置重复",
                            specialPattern.getUrl(), filedPattern.getFiled()));
                }
                filedMap.put(filedPattern.getFiled(), filedPattern.getPattern());
            }
            patternMap.put(specialPattern.getUrl(), filedMap);
        }
        return patternMap;
    }

    @Data
    public static class SpecialPattern implements Serializable {
        private String url;
        private List<FiledPattern> patterns;
    }

    @Data
    public static class FiledPattern implements Serializable {
        private String filed;
        private String pattern;
    }

}
