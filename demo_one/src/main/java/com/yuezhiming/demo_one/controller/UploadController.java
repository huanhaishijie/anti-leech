package com.yuezhiming.demo_one.controller;/**
 * Created by ASUSon 2020/4/22 19:13
 */

import com.yuezhiming.demo_one.filetype.FileType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.yuezhiming.demo_one.commons.ConversionUtil.getMultipartFile;
import static com.yuezhiming.demo_one.commons.FileTypeUtils.getFileType;

/**
 * @program: demo_one

 * @description: 读取文件流，防止木马文件上传

 * @author: yuezm

 * @create: 2020-04-22 19:13
 **/
@Controller
@RequestMapping("upload")
public class UploadController {

//....
    @RequestMapping("page")
    public String page(){
        return "upload";
    }


    @RequestMapping("")
    public void upload(HttpServletRequest request){
        List<MultipartFile> files = getMultipartFile.apply(request);
        files.forEach(file ->{
            try {
                InputStream inputStream = file.getInputStream();
                FileType type = getFileType.apply(inputStream);
                if(type == null) {System.out.println("未知类型的文件， 当前未收录这种文件类型"); return;}
                System.out.println("当前文件的类型为："+ type.getExt());
            }catch (Exception e){
                throw new RuntimeException(e.toString());
            }

        });

    }
}
