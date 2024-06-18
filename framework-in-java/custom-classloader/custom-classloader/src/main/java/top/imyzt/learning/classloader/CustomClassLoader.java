package top.imyzt.learning.classloader;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author imyzt
 * @date 2024/06/18
 * @description 自定义类加载器
 */
public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        byte[] bytes;
        try {
            bytes = getClassFromPath(name);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (bytes == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    private byte[] getClassFromPath(String name) throws IOException, URISyntaxException {
        Path path = Paths.get(this.getClass().getResource("/HelloWorld.class").toURI());
        return Files.readAllBytes(path);
    }
}
