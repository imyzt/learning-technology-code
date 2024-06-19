package top.imyzt.learning.spring.ai;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author imyzt
 * @date 2024/06/19
 * @description 描述信息
 */
@RestController
@RequestMapping
public class AiController {

    @Resource
    private ChatClient chatClient;

    @GetMapping("/ai/chat")
    Map<String, Object> chat(@RequestParam String question) {
        ChatClient.ChatClientRequest.CallPromptResponseSpec call = chatClient.prompt(new Prompt(question)).call();
        return Map.of("question", question, "answer", call.chatResponse());
    }
}
