package top.imyzt.learning.spi;


import java.util.ServiceLoader;

/**
 * @author imyzt
 * @date 2024/09/30
 * @description SPI Demo
 */
public class ServiceProviderDemo {

    public static void main(String[] args) {
        ServiceLoader<TextSearch> search = ServiceLoader.load(TextSearch.class);
        for (TextSearch textSearch : search) {
            var result = textSearch.search("keyword");
            System.out.println(result.toString());
        }
    }
}
