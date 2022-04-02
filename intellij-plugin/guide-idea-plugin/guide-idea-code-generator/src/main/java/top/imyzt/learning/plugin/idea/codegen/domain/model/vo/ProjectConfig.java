package top.imyzt.learning.plugin.idea.codegen.domain.model.vo;

import lombok.Data;

/**
 * @author imyzt
 * @date 2021/12/26
 * @description 项目配置信息
 */
@Data
public class ProjectConfig {

    private String _groupId;

    private String _artifactId;

    private String _version;

    private String _package;
}