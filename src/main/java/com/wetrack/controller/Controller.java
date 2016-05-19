package com.wetrack.controller;

import com.wetrack.domain.Base64ImageForm;
import com.wetrack.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Calendar;

/**
 * Created by zhanghong on 16/1/18.
 */
@org.springframework.stereotype.Controller
public class Controller implements InitializingBean{
    static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Value("${store.dir}")
    String storagePath;

    @Value("${store.dir}/${store.dir.image}")
    String imageLocation;

    @Value("${store.dir.image}")
    String imageFolder;

    @Autowired
    FileUploadService fileUploadService;

    /**
     * base64 + json 格式提交图片
     * @param form
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.TEXT_PLAIN_VALUE)
    String uploadImage(@RequestBody Base64ImageForm form, HttpServletResponse response) throws Exception{


        //文件的命名规则：当前时间戳＋线程id， 确保唯一，前提是该服务只存在一个进程，否则也会失败
        //TODO :命名规则可以配置
        String fileName = String.format("%s%s.%s", System.currentTimeMillis(), Thread.currentThread().getId(), form.getType());
        //文件夹按照年＋月的格式生成
        String folderName = String.format("%s%02d", Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1);

        //create folder if not exists
        File directory = new File(imageLocation, folderName);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        fileUploadService.uploadBase64Image(form, imageLocation + "/" + folderName, fileName);

        return imageFolder + "/" + folderName + "/" + fileName;
    }


    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    String entry(HttpServletResponse response) throws Exception{

        log.debug("entry");

        return "ok";
    }
//    @ResponseBody
//    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {"application/json"})
//    void postTest(@RequestBody Base64ImageForm test, HttpServletResponse response) throws Exception{
//
//        log.debug("entry");
//
//        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//        response.getOutputStream().println("ok");
//    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("controller initialized");
    }

}
