package top.imyzt.learning.springboot.v3.blog.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.springboot.v3.blog.model.Post;
import top.imyzt.learning.springboot.v3.blog.repository.PostRepository;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author imyzt
 * @date 2023/06/19
 * @description 控制器
 */
@RestController
@RequestMapping("post")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}