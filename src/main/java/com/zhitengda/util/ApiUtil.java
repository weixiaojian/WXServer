package com.zhitengda.util;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * api文档生成
 * @author langao_q
 * @since 2020-07-28 17:18
 */
public class ApiUtil {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        // 项目根目录
        config.setProjectPath("E:/idea-work/imwj/WXServer");
        // 项目名称
        config.setProjectName("WXServer");
        // 声明该API的版本
        config.setApiVersion("V2.0");
        // 生成API 文档所在目录
        config.setDocsPath("E:/idea-work/imwj/WXServer/api");
        // 配置自动生成
        config.setAutoGenerate(Boolean.TRUE);
        // 执行生成文档
        Docs.buildHtmlDocs(config);
    }

}
