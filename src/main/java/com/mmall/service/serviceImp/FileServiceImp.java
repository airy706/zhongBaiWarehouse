package com.mmall.service.serviceImp;

import com.google.common.collect.Lists;
import com.mmall.service.IService.IFileService;
import com.mmall.utils.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.protocol.ftp.FtpURLConnection;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service(value="iFileService")
public class FileServiceImp implements IFileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceImp.class);
    public String upload(MultipartFile file, String path){
        String fileName=file.getOriginalFilename();
        //获取文件拓展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        //重新UUID命名
        String uploadFileName=UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{},上传路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();//父目录不存在就一起创建
        }
        File targetFile=new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //此时文件已经上传了
            //文件上传至FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //将upload目录下的文件删除
            targetFile.delete();
        } catch (IOException e) {
           logger.error("文件上传失败",e);
           return null;
        }
        return targetFile.getName();
    }

}
