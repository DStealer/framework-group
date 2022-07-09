package cn.com.coderd.framework.provider.application.dubbo;

import cn.com.coderd.framework.service.application.dubbo.file.FileBiz;
import cn.com.coderd.framework.service.application.dubbo.file.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Slf4j
@DubboService(group = "", version = "", protocol = "hessian")
public class FileBizImpl implements FileBiz {
    @Override
    public InputStream transfer(FileDTO dto, InputStream inputStream) {
        log.info("receive dto:{}-{}", dto, inputStream);
        return new BufferedInputStream(inputStream);
    }
}
