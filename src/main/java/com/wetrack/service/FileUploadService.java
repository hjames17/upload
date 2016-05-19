package com.wetrack.service;


import com.wetrack.domain.Base64ImageForm;

/**
 * Created by zhanghong on 16/1/4.
 */
public interface FileUploadService {

    /**
     *
     * @param form
     * @param path
     * @param fileName
     * @throws Exception
     */
    void uploadBase64Image(Base64ImageForm form, String path, String fileName) throws Exception;
}
