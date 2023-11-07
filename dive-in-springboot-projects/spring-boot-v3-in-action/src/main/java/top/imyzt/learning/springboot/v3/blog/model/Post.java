package top.imyzt.learning.springboot.v3.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author imyzt
 * @date 2023/06/19
 * @description 博客内容
 */
@Entity
public class Post {

    @Id
    private Long id;

    private String title;

    private String userId;

    private String body;

    public Post() {
    }

    public Post(Long id, String title, String userId, String body) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }



    public void setBody(String content) {
        this.body = content;
    }
}