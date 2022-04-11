package top.imyzt.learning;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * @author imyzt
 * @date 2022/04/11
 * @description Spark-Java
 */
public class SparkJavaServer {

    public static void main(String[] args) {
        get("/hello", (req, resp) -> "Hello Spark Java!");
        post("/hello", (req, resp) -> "Hello Spark Java!");
        before((req, resp) -> System.out.printf("reqBefore, path=%s, param=%s%n", req.uri(), req.queryParams().toString()));
        after((req, resp) -> System.out.printf("reqAfter, path=%s, param=%s%n", req.uri(), req.queryParams().toString()));
    }
}