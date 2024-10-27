package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "common interface")
@Slf4j
public class CommonController {
    @PostMapping("/upload")
    @ApiOperation("upload file")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("upload file: {}",file);

        String filename = file.getOriginalFilename();
        System.out.println("filename: " + filename);

        InputStream inputStream = file.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        ServletContext context = request.getServletContext();
        String realPath = context.getRealPath("/uploadFolder");
        System.out.println("real path: " + realPath);
        File f = new File(realPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        String ext = filename.substring(filename.lastIndexOf("."));
        File targetFile = new File(f.getAbsolutePath() + "/" + UUID.randomUUID().toString() + ext);
        System.out.println("file absolute path: " + f.getAbsolutePath());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetFile));

        byte[] bytes = new byte[1024 * 10];
        int count = 0;
        while ((count = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, count);
        }

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        bufferedInputStream.close();


        return Result.success();
    }
}
