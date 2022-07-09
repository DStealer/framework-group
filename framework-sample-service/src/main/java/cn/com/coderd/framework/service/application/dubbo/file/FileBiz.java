package cn.com.coderd.framework.service.application.dubbo.file;


import java.io.InputStream;

public interface FileBiz {
    /**
     * dubbo文件传输
     *
     * @return
     */
    InputStream transfer(FileDTO dto, InputStream inputStream);
}
