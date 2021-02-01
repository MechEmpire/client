package com.mechempire.client.util;

import java.io.File;
import java.nio.file.Path;

/**
 * package: com.mechempire.client.util
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/2/1 下午10:12
 */
public class PathExUtil {
    
    public static String getFileName(Path path) {
        File file = new File(path.toString());
        String fullName = file.getName();
        return fullName.substring(0, fullName.lastIndexOf("."));
    }

    public static String getFileName(String fullName) {
        return fullName.substring(0, fullName.lastIndexOf("."));
    }

    public static String getExtName(Path path) {
        File file = new File(path.toString());
        String fullName = file.getName();
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }
}