package top.imyzt.learning.springboot.v3.blog.repository;

import org.springframework.data.repository.ListCrudRepository;
import top.imyzt.learning.springboot.v3.blog.model.Post;

/**
 * @author imyzt
 * @date 2023/06/19
 * @description 存储访问
 */
public interface PostRepository extends ListCrudRepository<Post, Long> {

}