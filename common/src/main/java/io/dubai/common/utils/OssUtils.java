package io.dubai.common.utils;

import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.oss.OSSFactory;
import org.springframework.web.multipart.MultipartFile;

public class OssUtils {

    public static String uploadToOss(MultipartFile file) {
        try{
            if (file.isEmpty()) {
                throw new RRException(ResponseStatusEnum.PLZ_CHOOSE_IMAGE);
            }
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
            return url;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadToOss(byte[] bytes,String suffix) {
        try{
            if (bytes == null || bytes.length < 1) {
                throw new RRException(ResponseStatusEnum.PLZ_CHOOSE_IMAGE);
            }
            String url = OSSFactory.build().uploadSuffix(bytes, suffix);
            return url;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
