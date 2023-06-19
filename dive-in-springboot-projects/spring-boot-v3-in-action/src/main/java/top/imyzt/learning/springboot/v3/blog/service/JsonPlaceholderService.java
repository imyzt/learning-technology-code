package top.imyzt.learning.springboot.v3.blog.service;

import org.springframework.web.service.annotation.GetExchange;
import top.imyzt.learning.springboot.v3.blog.model.Post;

import java.util.List;

/**
 * @author imyzt
 * @date 2023/06/19
 * @description 通过列出感兴趣的数据来监听, 类似于feign
 */
public interface JsonPlaceholderService {

    @GetExchange("/posts")
    List<Post> loadPosts();
}