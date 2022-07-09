package cn.com.coderd.framework.starter.web;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * spring mvc 流下载转换器
 */
@Slf4j
public class InputStreamMessageConverter extends AbstractGenericHttpMessageConverter<InputStream> {
    public InputStreamMessageConverter() {
        super(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return InputStream.class.isAssignableFrom(clazz);
    }

    @Override
    protected void writeInternal(InputStream inputStream, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().remove(HttpHeaders.ACCEPT_RANGES);
        try (OutputStream outputStream = outputMessage.getBody()) {
            StreamUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            logger.error("下载失败", e);
            outputMessage.getHeaders().clear();
        } finally {
            inputStream.close();
        }
    }

    @Override
    protected InputStream readInternal(Class<? extends InputStream> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        if (clazz.isAssignableFrom(inputMessage.getBody().getClass())) {
            return inputMessage.getBody();
        }
        try {
            Constructor<? extends InputStream> constructor = clazz.getConstructor(InputStream.class);
            return constructor.newInstance(inputMessage.getBody());
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("无法转换消息", e, inputMessage);
        }
    }

    @Override
    public InputStream read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return inputMessage.getBody();
    }
}
