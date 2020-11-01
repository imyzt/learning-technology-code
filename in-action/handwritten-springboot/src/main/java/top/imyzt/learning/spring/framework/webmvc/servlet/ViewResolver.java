package top.imyzt.learning.spring.framework.webmvc.servlet;

import cn.hutool.core.util.StrUtil;

import java.io.File;

/**
 * @author imyzt
 * @date 2020/10/31
 * @description 视图解析器
 */
public class ViewResolver {

    private File templateRootDir;

    private static final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    public ViewResolver(String templateRoot) {

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        templateRootDir = new File(templateRootPath);
    }

    public View resolverViewName(String viewName) {

        if (StrUtil.isBlank(viewName)) {
            return null;
        }

        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);

        File file = new File((this.templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new View(file);
    }
}