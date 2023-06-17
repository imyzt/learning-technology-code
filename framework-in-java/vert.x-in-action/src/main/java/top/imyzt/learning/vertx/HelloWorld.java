package top.imyzt.learning.vertx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2023/06/11
 * @description demo1
 */
@Slf4j
public class HelloWorld {

    @SneakyThrows
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(2));


        vertx.setPeriodic(1000, id -> {
            log.info("timer fired.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(8888));
        httpServer.requestHandler(req -> {
            log.info("path={}", req.path());
            req.response().end("hello world.");
        });
        httpServer.listen(8888);


        FileSystem fs = vertx.fileSystem();
        Future<Void> future = fs
                .createFile("vertx.txt")
                .compose(v -> fs.writeFile("vertx.txt", Buffer.buffer("hello")))
                .compose(v -> fs.move("vertx.txt", "vertx_new.txt"));
        log.info("future result={}", future.result());


    }
}