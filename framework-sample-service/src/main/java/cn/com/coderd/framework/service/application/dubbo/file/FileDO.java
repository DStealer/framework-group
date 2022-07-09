package cn.com.coderd.framework.service.application.dubbo.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FileDO extends InputStream implements Serializable {


    private String filename;
    private InputStream inputStream;

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
