package com.mechempire.client.manager;

import com.mechempire.client.util.PathExUtil;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * package: com.mechempire.client.manager
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:51
 */
@Slf4j
@Component
public class ResourceManager {

    private final HashMap<String, String> fonts = new HashMap<>(16);

    private final HashMap<String, String> images = new HashMap<>(16);

    private final HashMap<String, String> medias = new HashMap<>(16);

    private boolean inJar = false;

    private final String jarPath;

    public ResourceManager() {
        jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.endsWith(".jar")) {
            inJar = true;
            log.info("running in jar.");
        } else {
            inJar = false;
            log.info("running not in jar.");
        }

        loadResource("images", images);
        loadResource("medias", medias);
    }

    /**
     * 获取资源流
     *
     * @param resourcePath 资源路径
     * @return 输入流
     */
    public InputStream getResourceStream(String resourcePath) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (null == stream) {
            log.error("create InputStream {} failed.", resourcePath);
            return null;
        }

        return stream;
    }

    /**
     * 获取一个字体
     *
     * @param fontFamily 字体名称
     * @param fontSize   字体大小
     * @return 字体对象
     */
    public Font getFont(String fontFamily, double fontSize) {
        if (fonts.containsKey(fontFamily)) {
            String fontPath = fonts.get(fontFamily);
            InputStream stream = getResourceStream(fontPath);
            if (null != stream) {
                return Font.loadFont(stream, fontSize);
            }
        }
        log.error("font {} is not exist!", fontFamily);
        return Font.getDefault();
    }

    /**
     * 获取图片对象
     *
     * @param imageName 图片名称
     * @return 图片对象
     */
    public Image getImage(String imageName) {
        if (images.containsKey(imageName)) {
            String imagePath = images.get(imageName);
            InputStream stream = getResourceStream(imagePath);
            if (null != stream) {
                return new Image(stream);
            }
        }
        log.error("image {} is not exist!", imageName);
        return null;
    }

    /**
     * 获取音频资源
     *
     * @param musicName 音频名称
     * @return 媒体对象
     */
    public Media getMedia(String musicName) {
        if (medias.containsKey(musicName)) {
            String relativePath = medias.get(musicName);
            URL musicRes = getClass().getClassLoader().getResource(relativePath);
            if (null == musicRes) {
                log.error("cannot get media resource {}", relativePath);
                return null;
            }

            if (inJar) {
                String path = String.format("jar:file:%s!/%s", jarPath, relativePath);
                return new Media(path);
            } else {
                return new Media("file:" + musicRes.getPath());
            }
        }

        return null;
    }

    /**
     * 加载资源
     *
     * @param resourceType 资源类型
     * @param container    资源容器
     */
    private void loadResource(String resourceType, HashMap<String, String> container) {
        if (inJar) {
            loadResourceFromInner(resourceType, container);
        } else {
            loadResourceFromOuter(resourceType, container);
        }
    }

    /**
     * 从 jar 文件内部读取资源
     *
     * @param resourceType 资源类型
     * @param container    资源容器
     */
    private void loadResourceFromInner(String resourceType, HashMap<String, String> container) {
        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            JarFile localJarFile = new JarFile(new File(path));

            Enumeration<JarEntry> entries = localJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String innerPath = jarEntry.getName();
                if (innerPath.startsWith(resourceType) && !innerPath.endsWith("/")) {
                    File resFile = new File(innerPath);
                    container.put(PathExUtil.getFileName(resFile.getName()), innerPath);
                }
            }
        } catch (Exception e) {
            log.error("loadResourceFromInner error: {}", e.getMessage(), e);
        }
    }

    /**
     * 从外部的资源文件夹读取资源
     *
     * @param resourceType 资源类型
     * @param container    资源容器
     */
    private void loadResourceFromOuter(String resourceType, HashMap<String, String> container) {
        URL url = getClass().getClassLoader().getResource(resourceType);
        if (null == url) {
            log.error("{} resources is not exist!", resourceType);
            return;
        }

        try {
            File resourceDir = new File(url.toURI());
            File[] resources = resourceDir.listFiles();
            if (null == resources) {
                log.error("there is no resource in resource type '{}'", resourceType);
                return;
            }

            for (File res : resources) {
                container.put(PathExUtil.getFileName(res.getName()), resourceType + "/" + res.getName());
            }

        } catch (Exception e) {
            log.error("loadResourceFromOuter error: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取资源文件的相对路径
     *
     * @param resourceType 资源类型
     * @param resourceName 资源名称
     * @return 返回资源文件的相对路径
     */
    private String getResource(String resourceType, String resourceName) {
        switch (resourceType) {
            case "fonts":
                if (fonts.containsKey(resourceName)) {
                    return fonts.get(resourceName);
                }
                break;
            case "images":
                if (images.containsKey(resourceName)) {
                    return images.get(resourceName);
                }

                break;
            default:
                log.error("Resource Type {} is not exist!", resourceType);
        }

        return null;
    }
}