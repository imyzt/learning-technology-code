package top.imyzt.learning.spring.framework.webmvc.servlet;

import java.util.Map;

/**
 * @author imyzt
 * @date 2020/10/31
 * @description 描述信息
 */
public class ModalAndView {

    private String viewName;

    private Map<String, ?> modal;

    public ModalAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModalAndView(String viewName, Map<String, ?> modal) {
        this.viewName = viewName;
        this.modal = modal;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModal() {
        return modal;
    }

    public void setModal(Map<String, ?> modal) {
        this.modal = modal;
    }
}