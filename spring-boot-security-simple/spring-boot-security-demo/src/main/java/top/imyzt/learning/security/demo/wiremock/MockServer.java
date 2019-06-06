package top.imyzt.learning.security.demo.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/6/5
 * @description MockServer
 */
public class MockServer {

    public void main(String[] args) throws IOException {
//    public static void main(String[] args) throws IOException {

        // 连接服务器
        WireMock.configureFor(8062);
        // 删除之前所有的映射
        WireMock.removeAllMappings();

        mock("/order/1", "01");
        mock("/order/2", "02");
    }

    private static void mock(String testUrl, String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/" + fileName + ".json");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(), "UTF-8").toArray(), "\n");
        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo(testUrl))
                        .willReturn(WireMock.aResponse()
                        .withBody(content)
                        .withStatus(HttpStatus.OK.value())));
    }
}
