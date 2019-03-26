package project.ys.glass_system.util;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ys
 * @date on 2019/1/19 21:54
 */
public class FileUtil {
    /**
     * 绝对路径
     **/
    public static String absolutePath = "";

    /**
     * 静态目录
     **/
    public static String staticDir = "/upload/";

    public static String upload(MultipartFile file, String uuid) throws IOException {
        //第一次会创建文件夹
        createDirIfNotExists();

        String resultPath = staticDir + uuid + getSuffix(file.getOriginalFilename());
        System.out.println("resultPath is " + resultPath);
        //存文件
        File uploadFile = new File(absolutePath, resultPath);
        file.transferTo(uploadFile);
        return resultPath;
    }

    public static File download(String path) {
        createDirIfNotExists();
        System.out.println("path is " + path);
        ClassLoader classLoader = new ClassLoader();
        classLoader.getResource(path);
        File downloadFile = new File(absolutePath,path);
        return downloadFile;
    }


    /**
     * 创建文件夹路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createDirIfNotExists() {
        if (!absolutePath.isEmpty()) {
            return;
        }

        //获取跟目录
        File file;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取根目录失败，无法创建上传目录！");
        }
        if (!file.exists()) {
            file = new File("");
        }

        absolutePath = file.getAbsolutePath();

        File upload = new File(absolutePath, staticDir);
        if (!upload.exists()) {
            upload.mkdirs();
        }
    }

    public static boolean delete(String uuid, String suffix) {
        File file = new File(absolutePath, staticDir + uuid + suffix);
        return file.exists() && file.delete();
    }

    public static String getSuffix(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        int dotIndex = fileName.indexOf(".");
        return fileName.substring(dotIndex, fileName.length());
    }


}
