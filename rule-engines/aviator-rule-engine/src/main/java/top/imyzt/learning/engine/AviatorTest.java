package top.imyzt.learning.engine;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author imyzt
 * @date 2021/02/02
 * @description 描述信息
 */
public class AviatorTest {

    public static void main(String[] args) {

        JSONObject json = new JSONObject();
        json.put("test", 1);

        JSONObject json2 = new JSONObject();
        json2.put("name", "yzt");
        json.put("json2", json2);

        System.out.println(json.toJSONString());

        Object result = AviatorEvaluator.execute("test==1", json);
        System.out.println(result);

        Object result2 = AviatorEvaluator.execute("test==2", json);
        System.out.println(result2);

        Object result3 = AviatorEvaluator.execute("json2.name == 'yzt'", json);
        System.out.println(result3);

        Object result4 = AviatorEvaluator.execute("json2.name == 'xxx'", json);
        System.out.println(result4);
    }
}