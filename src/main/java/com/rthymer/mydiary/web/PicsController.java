package com.rthymer.mydiary.web;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class PicsController {
    @RequestMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空哦。";
        }

        String fileName = file.getOriginalFilename();
        String filePath = "/home/rthymer/Documents/DiaryPics/";
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return "上传成功了！";
        } catch (IllegalStateException e) {
            return "上传失败了哟:非法状态" + e.getMessage();
        } catch (IOException e) {
            return "上传失败了哟:IO错误" + e.getMessage();
        }
    }

    @RequestMapping(value = "/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("filename");
        String realPath = "/home/rthymer/Documents/DiaryPics/";
        if (fileName != null) {
            File fileOrigin = new File(realPath, fileName);
            File fileScale = new File(realPath, "scaled_" + fileName);
            scalePics(fileOrigin, fileScale);
            if (fileScale.exists()) {
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(fileScale);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        return null;
    }

    private void scalePics(File sourcePath, File destinationPath) {
        try {
            Thumbnails.of(sourcePath).size(300, 200).toFile(destinationPath);
        } catch (IOException e) {
            throw new RuntimeException("Error while scaling pics...");
        }
    }

}
