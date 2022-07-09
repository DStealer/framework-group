package cn.com.coderd.framework.service.application.dubbo.file;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FileDTO implements Serializable {
    private String filename;
}
