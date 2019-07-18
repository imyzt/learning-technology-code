package top.imyzt.plugin;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.imyzt.plugin.domain.Youdao;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TranslationHelper extends AnAction {

    private static Logger log = LoggerFactory.getLogger(TranslationHelper.class);

    private static String TRANSLATION_URL = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=%s";



    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here


        // 获取用户当前编辑器对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (null == editor) {
            // 当前没有选中
            return;
        }

        // 通过编辑器获取用户选择对象
        SelectionModel selectionModel = editor.getSelectionModel();

        // 用户选中的文本
        String text = selectionModel.getSelectedText();

        if (StringUtils.isBlank(text)) {
            return;
        }

        final String translationText = youdaoTranslation(text);

        if (null != translationText) {

            // 回到界面渲染层显示对话框, 不可以在其它线程打印
            // 显示气泡形式结果
            System.out.println(translationText);
            showPopup(editor, translationText);
        }
    }

    private void showPopup(Editor editor, String translationText) {
        ApplicationManager.getApplication().invokeLater(() -> {
            // 获取默认popup工厂
            JBPopupFactory popupFactory = JBPopupFactory.getInstance();
            BalloonBuilder builder = popupFactory.createHtmlTextBalloonBuilder(translationText, null,
                    new JBColor(new Color(188, 238, 188), new Color(73, 120, 73)), null);

            builder.setFadeoutTime(3000) // 三秒无操作
                    .createBalloon()  // 创建气泡
                    .show(popupFactory.guessBestPopupLocation(editor), Balloon.Position.below); //指定位置(editor对象即目前选中的内容),并显示在下方
        });
    }


    private String youdaoTranslation(String text) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(String.format(TRANSLATION_URL, text));
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                //内容写入文件
                System.out.println("内容长度："+content.length());
                return getTranslationContentTarget(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //相当于关闭浏览器
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private String getTranslationContentTarget(String content) {
        Gson gson = new Gson();
        Youdao youdao = gson.fromJson(content, Youdao.class);
        if (null != youdao) {
            List<List<Youdao.TranslateResultBean>> translateResult = youdao.getTranslateResult();
            if (CollectionUtils.isNotEmpty(translateResult)) {
                List<Youdao.TranslateResultBean> translateResultBeans = translateResult.get(0);
                if (CollectionUtils.isNotEmpty(translateResultBeans)) {
                    Youdao.TranslateResultBean translateResultBean = translateResultBeans.get(0);
                    return translateResultBean.getTgt();
                }
            }
        }
        return null;
    }
}
