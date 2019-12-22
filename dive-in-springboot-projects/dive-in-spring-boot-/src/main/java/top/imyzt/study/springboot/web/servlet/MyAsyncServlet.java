package top.imyzt.study.springboot.web.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/12/22
 * @description 异步servlet在SpringBoot中的使用
 */
@WebServlet(value = "/my/asyncServlet", asyncSupported = true)
public class MyAsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        // 获取到异步上下文
        AsyncContext asyncContext = req.startAsync();

        asyncContext.start(() -> {

            try {
                resp.getWriter().println("hello world, async");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 触发完成
            asyncContext.complete();
        });
    }
}
