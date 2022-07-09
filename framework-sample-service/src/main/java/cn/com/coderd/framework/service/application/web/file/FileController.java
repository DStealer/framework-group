package cn.com.coderd.framework.service.application.web.file;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RequestMapping("/file")
public interface FileController {
    /**
     * 文件上传
     *
     * @param param1
     * @param file
     * @return
     */
    @PostMapping("/transfer")
    ResponseEntity<InputStream> transfer(@RequestParam("param1") String param1, @RequestParam("file") MultipartFile file);
}
