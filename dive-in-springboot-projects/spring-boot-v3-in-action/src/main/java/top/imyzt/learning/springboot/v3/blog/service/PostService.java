package top.imyzt.learning.springboot.v3.blog.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.imyzt.learning.springboot.v3.blog.model.Post;

import java.util.List;

/**
 * @author imyzt
 * @date 2023/06/19
 * @description 服务类
 */
@Service
public class PostService {

    public static final String URL = "http://jsonplaceholder.typicode.com/posts";
    private final RestTemplate restTemplate;

    public PostService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Post> loadPosts() {
        ResponseEntity<List<Post>> exchange = restTemplate.exchange(URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return exchange.getBody();
    }
}