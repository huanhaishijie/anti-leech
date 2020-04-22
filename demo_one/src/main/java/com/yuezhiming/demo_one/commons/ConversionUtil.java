package com.yuezhiming.demo_one.commons;
/**
 * Created by ASUSon 2019/12/26 17:07
 */

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * @program: wx-lims-ext

 * @description: 转换工具、文件签名

 * @author: yuezm

 * @create: 2019-12-26 17:07
 **/
public  class ConversionUtil<T> {

	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 自定义转换，内部可以自己实现
     */
    public  Function<T,T> conversion = t -> t;

     public static Function<String, String>  markConversion = i ->{
         if(i == null || i.equals(""))
             return null;
        return  i.replace("“","\"").replace("”","\"").replace("‘","'").
                replace("’","'").replace("《","(").replace("》",")").
                replace("（","(").replace("）",")").replace("：",":").
                replace("；",";").replace("。",".");
    };

    public static Function<String, LocalDate> dataconversion = date ->{ //日期格式校验
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    };

   public static Function<String, Long> numconversion = num ->{ //数字格式校验
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(num);
        if(!isNum.matches()){
            return null;
        }
        return Long.parseLong(num);
    };

   public static Function<Object, String> strconversion = str ->{
       return str == null ? "" : str.toString();
   };

  static BiFunction<List<Map<String, Object>>, Map<String, Object>, Boolean> mapIsExist = (origin, target) ->{
       AtomicInteger counter = new AtomicInteger(0);
       origin.stream().forEach(item ->{
           AtomicBoolean mark = new AtomicBoolean(true);
           target.keySet().stream().forEach(l ->{
               if(!item.get(l).equals(target.get(l))){
                    mark.set(false);
                    return;
               }
           });
           if(mark.get()){
               counter.set((counter.get() + 1));
           }
       });
       return counter.get() > 0;
   };

    //未知大小的list获取值防止抛异常 1;
    public static void mapjoinValue(Map<String, Object> container, List<String> values, String key, Integer index){
        try {
            container.put(key, values.get(index) == null ? "" : values.get(index));
        }catch (Exception e){
            e.printStackTrace();
            container.put(key, null);
        }
    }

    //未知大小的list获取值防止抛异常 2; keys 和 values 相对应排列,
   public static void mapjoinValue(Map<String, Object> container, List<String> values, List<String> keys){
        IntStream.range(0, values.size()).forEach(i ->{
            try {
                container.put(keys.get(i), values.get(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        if(keys.size() > values.size()){
            IntStream.range(values.size(), keys.size()).forEach(i ->{
                container.put(keys.get(i), null);
            });
        }
    }



	public static Function<HttpServletRequest, List<MultipartFile>> getMultipartFile = res ->{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) res;
		/** 页面控件的文件流* */
		List<MultipartFile> multipartFiles = new ArrayList<>();
		Map<String, MultipartFile> map = multipartRequest.getFileMap();

		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			multipartFiles.add(map.get(obj));
		}
		return multipartFiles;
	};

    public static BiFunction<StringBuilder, String, StringBuilder> strcontain = (str1, str2) -> str1.toString().contains(str2) == true ? str1 : str1.append(str2);


	/**
	 * md5文件签名
	 */
	/**
	 * 计算MD5校验
	 * @param buffer
	 * @return 空串，如果无法获得 MessageDigest实例
	 */
	private static Function<ByteBuffer, String> MD5 = buffer -> {
		String s = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buffer);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>,
				// 逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	};

	private static String MD5(byte[] bytes){
		String s = "";
		try{
			MessageDigest  md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>,
				// 逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Get MD5 of a file (lower case)
	 * @return empty string if I/O error when get MD5
	 */


	public static Function<File, String> fileMD5  = file ->{
		if (file == null) throw new RuntimeException("传入文件为空");
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			return MD5.apply(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	};

	public static Function<MultipartFile, String> multipartFileMD5  = file ->{
		if(file == null) throw new RuntimeException("上传文件为空");
		try {
			return MD5(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	return "";
//		if (file == null) throw new RuntimeException("传入文件为空");
//		try (FileInputStream in = (FileInputStream) file.getInputStream()) {
//			InputStream inputStream = file.getInputStream();
//			BufferedInputStream bis = new BufferedInputStream(inputStream);
//			file.getBytes();
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			FileChannel ch = in.getChannel();
//			return MD5.apply(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.getSize()));
//		}catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return "";
	};

}
