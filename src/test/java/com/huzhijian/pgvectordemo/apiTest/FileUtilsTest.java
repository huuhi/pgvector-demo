package com.huzhijian.pgvectordemo.apiTest;

import org.junit.jupiter.api.Test;
import static org.codelibs.jhighlight.tools.FileUtils.getExtension;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/27
 * 说明:
 */
public class FileUtilsTest {
    @Test
    void getExtensionTest(){
        String fileName="abc.pdf";
        String extension = getExtension(fileName);
        System.out.println(extension);
    }

    @Test
    void getPath(){
        System.out.println(System.getProperty("user.home"));
    }
}
