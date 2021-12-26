package top.imyzt.learning.plugin.idea.codegen.domain.service;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 描述信息
 */
public class FreemarkerConfiguration extends Configuration {

    public FreemarkerConfiguration() {
        this("/template");
    }

    public FreemarkerConfiguration(String basePackagePath) {
        super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        setDefaultEncoding("UTF-8");
        setClassForTemplateLoading(getClass(), basePackagePath);
    }

    @Override
    public Template getTemplate(String ftl) throws IOException {
        return this.getTemplate(ftl, "UTF-8");
    }
}