package cn.com.coderd.framework.gateway.constants;

import org.springframework.http.MediaType;

/**
 * 自定义加密媒体类型
 */
public interface MediaTypeConstants {
    /**
     * JSONM媒体类型
     */
    MediaType MEDIA_TYPE_JSONM = new MediaType("application", "jsonm");
    /**
     * JSONX 媒体类型
     */
    MediaType MEDIA_TYPE_JSONX = new MediaType("application", "jsonx");
}
