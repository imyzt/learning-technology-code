package top.imyzt.learning.springboot.v3.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import top.imyzt.learning.springboot.v3.blog.model.Post;
import top.imyzt.learning.springboot.v3.blog.repository.PostRepository;
import top.imyzt.learning.springboot.v3.blog.service.JsonPlaceholderService;
import top.imyzt.learning.springboot.v3.blog.service.PostService;

import java.util.List;

/**
 * @author imyzt
 */
@SpringBootApplication
public class SpringBoot3DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3DemoApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/*@Bean
	CommandLineRunner commandLineRunner(PostRepository postRepository, PostService postService) {
		return args -> {
			List<Post> posts = postService.loadPosts();
			System.out.println("load posts... len=" + posts.size());
			List<Post> saveAll = postRepository.saveAll(posts);
			System.out.println("saveAll=" + saveAll);
		};
	}*/

	@Bean
	CommandLineRunner commandLineRunner(PostRepository postRepository, PostService postService) {
		return args -> {

			WebClient webClient = WebClient.builder().baseUrl("http://jsonplaceholder.typicode.com").build();
			HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
			JsonPlaceholderService placeholderService = factory.createClient(JsonPlaceholderService.class);
			List<Post> posts = placeholderService.loadPosts();

			System.out.println("load posts... len=" + posts.size());
			List<Post> saveAll = postRepository.saveAll(posts);
			System.out.println("saveAll=" + saveAll);
		};
	}
}
