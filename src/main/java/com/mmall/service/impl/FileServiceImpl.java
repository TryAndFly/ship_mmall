package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtendsionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //对源文件进行重命名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtendsionName;
        logger.info("开始上传文件，上传文件名为：{},上传路径:{},新文件名{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            //不存在则创建
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件上传成功

            //todo 将tagerFile上传到ftp
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传

            //todo 删除upload下面的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传异常", e);
            e.printStackTrace();
            return null;
        }

        return targetFile.getName();
    }
}
