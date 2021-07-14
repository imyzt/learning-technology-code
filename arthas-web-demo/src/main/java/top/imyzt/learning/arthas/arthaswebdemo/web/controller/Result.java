package top.imyzt.learning.arthas.arthaswebdemo.web.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author imyzt
 * @date 2021/07/10
 * @description 描述信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer param;
    private Integer returnObj;
}