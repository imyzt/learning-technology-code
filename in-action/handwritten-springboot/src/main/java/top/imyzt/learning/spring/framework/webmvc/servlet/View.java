package top.imyzt.learning.spring.framework.webmvc.servlet;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imyzt
 * @date 2020/11/01
 * @description 描述信息
 */
public class View {

    private File viewFile;

    public View(File file) {
        this.viewFile = file;
    }

    public void reader(HttpServletRequest req, HttpServletResponse resp, Map<String, ?> modal) throws IOException {

        StringBuffer sb = new StringBuffer();

        RandomAccessFile ra = new RandomAccessFile(viewFile, "r");

        String line;
        while ((line = ra.readLine()) != null) {
            line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            Pattern compile = Pattern.compile("\\$\\{\\{[^}]+}}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = compile.matcher(line);
            while (matcher.find()) {
                String paramName = matcher.group();
                // 找到${{xxx}}表达式, 替换掉${{}}内容得到中间参数名xxx
                paramName = paramName.replaceAll("\\$\\{\\{|}}", "");
                // 从modal中找到参数值
                Object paramValue = modal.get(paramName);
                // 替换表达式为参数值
                line = matcher.replaceFirst(makeStrForRegExp(paramValue));
                // 继续匹配后面的表达式
                matcher = compile.matcher(line);
            }

            sb.append(line);
        }

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(sb.toString());
    }

    private String makeStrForRegExp(Object content) {
        if (Objects.isNull(content)) {
            return "";
        }

        return content.toString().replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("?", "\\?")
                .replace(",", "\\,")
                .replace(".", "\\.")
                .replace("&", "\\&");

    }
}