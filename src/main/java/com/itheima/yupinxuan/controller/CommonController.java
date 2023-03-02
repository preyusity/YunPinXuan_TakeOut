package com.itheima.yupinxuan.controller;

import com.itheima.yupinxuan.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.jvm.hotspot.runtime.Bytes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController@RequestMapping("/common")@Slf4j
public class CommonController {
    //引入application.yml中的路径
    @Value("${yupinxuan.path}")
    private String basePath;
    /**文件上传
    @param file
    @return
    */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String x=originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖 eg:uid.jpl
        String s = UUID.randomUUID() + x;
        //创建一个目录对象
        File file1=new File(basePath);
        //判断当前目录是否存在
        if (!file1.exists()){
            //目录不存在，需要创建
            file1.mkdirs();
        }
        //将临时文件转存到指定位置
        try {
            file.transferTo(new File(basePath+x));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return文件明
        return  R.success(x);
    }
    /**文件下载
     @param name
     @param response
     */
    @GetMapping("/download")
    public void  download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream inputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回客户端浏览器
            ServletOutputStream outputStream= response.getOutputStream();
            //规定输出格式"image/jpeg"
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            //len为-1时候为读取完
            while ((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                //从byte为0的地方开始写写到len
                outputStream.flush();
            }
            //关闭资源
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
