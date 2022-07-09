package cn.com.coderd.framework.consumer.application.web;

import cn.com.coderd.framework.service.application.dubbo.file.FileBiz;
import cn.com.coderd.framework.service.application.dubbo.file.FileDTO;
import cn.com.coderd.framework.service.application.web.file.FileController;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;

@Slf4j
@RestController
public class FileControllerImpl implements FileController {

    @DubboReference(group = "", version = "", check = false)
    private FileBiz biz;

    @Override
    public ResponseEntity<InputStream> transfer(String param1, MultipartFile file) {
        if (param1 == null || param1.isEmpty() || file == null) {
            return ResponseEntity.notFound().build();
        }
        try (InputStream inputStream = file.getInputStream()) {
            InputStream transfer = biz.transfer(new FileDTO().setFilename(param1), inputStream);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONNECTION, "close")
                    .header("Content-Disposition", "attachment;filename="
                            + URLEncoder.encode(param1, "UTF-8"))
                    .body(transfer);
        } catch (Exception e) {
            log.error("文件处理失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
