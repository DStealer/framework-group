package cn.com.coderd.framework.gateway.security;

import cn.com.coderd.framework.common.basic.Result;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关键字检查器
 */
@Slf4j
public class SecurityChecker implements InitializingBean {
    private static final ObjectMapper MAPPER = JsonMapper.builder().build();
    @Autowired
    private SecurityPatternsProperties properties;
    private boolean showDetail;
    private Pattern urlPattern;
    private Pattern htmlPattern;
    private Pattern jsPattern;
    private Pattern sqlPattern;
    private Map<String, Map<String, Pattern>> specialPatterns;


    /**
     * 检查内容
     * TODO 该检测目前不支持json数组,虽然可以做,但是不想做了
     *
     * @param request
     * @param content
     * @return
     */

    @SneakyThrows
    public Result<Void> checkJson(ServerHttpRequest request, String content) {
        Map<String, Pattern> fieldPatterns = this.specialPatterns.get(request.getURI().getPath());
        try (JsonParser parser = MAPPER.getFactory().createParser(content)) {
            while (parser.nextToken() != null) {
                JsonToken token = parser.getCurrentToken();
                switch (token) {
                    case FIELD_NAME: {
                        String text = parser.getText();
                        if (!StringUtils.isAlphanumeric(text)) {
                            if (this.showDetail) {
                                return Result.fail("GW400", String.format("检测关键字词根:[%s]路径:[%s]", text,
                                        parser.getParsingContext().pathAsPointer()));
                            } else {
                                return Result.fail("GW400", String.format("检测关键字词根:[%s]", text));
                            }
                        }
                    }
                    break;
                    case VALUE_STRING: {
                        String text = parser.getText();
                        if (fieldPatterns != null) {
                            String pathAsPointer = this.toJsonPoint(parser.getParsingContext());
                            Pattern fieldPattern = fieldPatterns.get(pathAsPointer);
                            if (fieldPattern != null) {
                                Matcher fieldMatcher = fieldPattern.matcher(text);
                                if (fieldMatcher.find()) {
                                    if (this.showDetail) {
                                        return Result.fail("GW400", String.format("检测FIELD关键字:[%s]匹配:[%s]路径:[%s]:报文:[%s]", text, fieldMatcher.group(),
                                                parser.getParsingContext().pathAsPointer(), content));
                                    } else {
                                        return Result.fail("GW400", String.format("检测FIELD关键字:%s", text));
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                        Matcher urlMatcher = this.urlPattern.matcher(text);
                        if (urlMatcher.find()) {
                            if (this.showDetail) {
                                return Result.fail("GW400", String.format("检测URL关键字:[%s]匹配:[%s]路径:[%s]:报文:[%s]", text, urlMatcher.group(),
                                        parser.getParsingContext().pathAsPointer(), content));
                            } else {
                                return Result.fail("GW400", String.format("检测URL关键字:%s", text));
                            }
                        }
                        Matcher htmlMatcher = this.htmlPattern.matcher(text);
                        if (htmlMatcher.find()) {
                            if (this.showDetail) {
                                return Result.fail("GW400", String.format("检测HTML关键字:[%s]匹配:[%s]路径:[%s]:报文:[%s]", text, htmlMatcher.group(),
                                        parser.getParsingContext().pathAsPointer(), content));
                            } else {
                                return Result.fail("GW400", String.format("检测HTML关键字:%s", text));
                            }
                        }
                        Matcher jsMatcher = this.jsPattern.matcher(text);
                        if (jsMatcher.find()) {
                            if (this.showDetail) {
                                return Result.fail("GW400", String.format("检测JS关键字:[%s]匹配:[%s]路径:[%s]:报文:[%s]", text, jsMatcher.group(),
                                        parser.getParsingContext().pathAsPointer(), content));
                            } else {
                                return Result.fail("GW400", String.format("检测JS关键字:%s", text));
                            }
                        }
                        Matcher sqlMatcher = this.sqlPattern.matcher(text);
                        if (sqlMatcher.find()) {
                            if (this.showDetail) {
                                return Result.fail("GW400", String.format("检测SQL关键字:[%s]匹配:[%s]路径:[%s]:报文:[%s]", text, sqlMatcher.group(),
                                        parser.getParsingContext().pathAsPointer(), content));
                            } else {
                                return Result.fail("GW400", String.format("检测SQL关键字:%s", text));
                            }
                        }
                    }
                    break;
                }
            }
        }
        return Result.ok(null);
    }

    public String toJsonPoint(JsonStreamContext context) {
        StringBuilder builder = new StringBuilder();
        for (JsonStreamContext ctx = context; ctx != null; ctx = ctx.getParent()) {
            if (ctx.inObject()) {
                String seg = ctx.getCurrentName();
                if (seg != null) {
                    builder.insert(0, "/" + seg);
                } else {
                    builder.insert(0, "/?");
                }
            } else if (ctx.inArray()) {
                builder.insert(0, "/*");
            }
        }
        return builder.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("构建敏感字过滤规则检查器");
        this.showDetail = this.properties.isShowDetail();
        this.urlPattern = Pattern.compile(this.properties.getUrlPattern(), Pattern.CASE_INSENSITIVE);
        this.htmlPattern = Pattern.compile(this.properties.getHtmlPattern(), Pattern.CASE_INSENSITIVE);
        this.jsPattern = Pattern.compile(this.properties.getJsPattern(), Pattern.CASE_INSENSITIVE);
        this.sqlPattern = Pattern.compile(this.properties.getSqlPattern(), Pattern.CASE_INSENSITIVE);
        Map<String, Pattern> cachePatternMap = new HashMap<>();
        this.specialPatterns = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : this.properties.convertSpecialPatterns().entrySet()) {
            Map<String, Pattern> map = this.specialPatterns.computeIfAbsent(URI.create(entry.getKey()).getPath(), key -> new HashMap<>());
            for (Map.Entry<String, String> et : entry.getValue().entrySet()) {
                Pattern pattern = cachePatternMap.computeIfAbsent(et.getValue(), k -> Pattern.compile(k, Pattern.CASE_INSENSITIVE));
                map.put(et.getKey(), pattern);
            }
        }
    }
}
