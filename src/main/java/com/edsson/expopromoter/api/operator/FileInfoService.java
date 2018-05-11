package com.edsson.expopromoter.api.operator;

import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class FileInfoService {

//    public FileInfoService() {
//    }

    public  static String findFileExtension(String base64) throws IOException {

        byte[] base64Bytes = Base64.decodeBase64(base64);
        Tika tika = new Tika();
        String fileType = tika.detect(base64Bytes);
        String fileExtension = changeFileType(fileType);
        return ("eventImage" +  "." +fileExtension);
    }

    public static String changeFileType(String fileType) {

        boolean cahnged = false;
        if (fileType.subSequence(0, 5).equals("image")) {
            fileType = fileType.substring(6, fileType.length());
        }
        if (fileType.toLowerCase().contains("PDF".toLowerCase())) {
            fileType = "pdf";
            cahnged = true;
        }
        if (fileType.toLowerCase().contains("msword")) {
            fileType = "doc";
            cahnged = true;
        }
        if (fileType.toLowerCase().contains("wordprocessingml")) {
            fileType = "docx";
            cahnged = true;
        }

        if (fileType.toLowerCase().contains("excel")) {
            fileType = "xls";
            cahnged = true;
        }
        if (cahnged){
//            log.debug("Changed fileType to: ["+fileType+"]");
        }
        return fileType;
    }
}
