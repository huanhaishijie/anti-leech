package com.yuezhiming.demo_one.commons;/**
 * Created by ASUSon 2020/4/22 19:35
 */

import com.yuezhiming.demo_one.filetype.FileType;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-22 19:35
 **/
public class FileTypeUtils {
    public static Function<InputStream, FileType> getFileType = is -> {
        try {
            byte[] src = new byte[28];
            is.read(src, 0, 28);
            StringBuilder stringBuilder = new StringBuilder("");
            if(src == null || src.length <= 0){
                return null;
            }
            for(int i = 0; i < src.length; i++){
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v).toUpperCase();
                if(hv.length() < 2){
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            FileType[] fileTypes = FileType.values();
            for(FileType f : fileTypes){
                if(stringBuilder.toString().startsWith(f.getValue())){
                    return f;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        return null;
    };

}
