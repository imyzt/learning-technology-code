package top.imyzt.learning.transaction.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import top.imyzt.learning.transaction.order.OrderServiceApplication;
import top.imyzt.learning.transaction.order.pojo.entity.Order;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrderServiceApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class OrderServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {

        Order order = new Order();
        order.setCommodityCode("TAOBAO");
        order.setCount(1);
        order.setUserId("imyzt");
        order.setMoney(2);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(order));

        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}