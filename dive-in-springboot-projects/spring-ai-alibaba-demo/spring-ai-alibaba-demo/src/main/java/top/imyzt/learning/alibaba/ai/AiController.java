package top.imyzt.learning.alibaba.ai;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author imyzt
 * @date 2024/06/20
 * @description AI 入口
 */
@Slf4j
@RestController
@RequestMapping
public class AiController {

    @Resource
    private ChatClient chatClient;
    @Resource
    private ImageClient imageClient;

    @GetMapping("/ai/chat")
    public String chat(@RequestParam String question) {
        ChatResponse call = chatClient.call(new Prompt(question));
        return call.getResult().getOutput().getContent();
    }

    @GetMapping("/ai/aigc")
    public String aigc(@RequestParam String question) {
        ImageResponse call = imageClient.call(new ImagePrompt(question));
        return call.getResult().getOutput().getUrl();
    }
}
