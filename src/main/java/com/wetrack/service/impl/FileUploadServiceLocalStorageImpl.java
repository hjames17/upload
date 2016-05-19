package com.wetrack.service.impl;


import com.wetrack.domain.Base64ImageForm;
import com.wetrack.service.FileUploadService;
import com.wetrack.util.ImageUtils;
import org.springframework.stereotype.Service;

/**
 * Created by zhanghong on 16/1/4.
 */
@Service
public class FileUploadServiceLocalStorageImpl implements FileUploadService {


    public void uploadBase64Image(Base64ImageForm form, String path, String fileName) throws Exception {
        ImageUtils.saveBase64ImageToFile(path, fileName, form.getType(), form.getData(), form.getX(), form.getY()
                , form.getW(), form.getY(), form.getScale(), form.isCut());


    }
}