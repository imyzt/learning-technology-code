package top.imyzt.jdk.features.java21;


import java.util.Iterator;

/**
 * @author imyzt
 * @date 2023/12/20
 * @description 自定义StringTemplate
 */
public class CustomTemplate {

    public static void main(String[] args) {

        var INTER = StringTemplate.Processor.of((StringTemplate st) -> {
            StringBuilder sb = new StringBuilder();
            Iterator<String> fragments = st.fragments().iterator();
            for (Object value : st.values()) {
                sb.append(fragments.next());
                sb.append(value);
            }
            sb.append(fragments.next());
            return sb.toString();
        });

        String name = "yzt";
        int index = 0;
        String text = INTER."""
                {
                "name":\{name},
                "index":\{++index}
                }
                """;
        System.out.println(text);
        //{
        //"name":yzt,
        //"index":1
        //}
    }
}
