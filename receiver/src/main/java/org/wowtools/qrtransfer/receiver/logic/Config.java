package org.wowtools.qrtransfer.receiver.logic;

import org.wowtools.common.utils.PropertiesReader;
import org.wowtools.qrtransfer.receiver.StartReceiver;

import java.io.File;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class Config {
    /**
     * 存储文件名(绝对路径)
     */
    public static File dest;

    /**
     * 每刷新一页后延迟多少毫秒，默认0，如果图像有延迟可适当设置此参数
     */
    public static long pageDelay;

    public static int scanX = 0;
    public static int scanY = 0;
    public static int scanW = 512;


    public volatile static boolean autoPressKey = true;

    static {

        PropertiesReader p = new PropertiesReader(StartReceiver.class, "config.properties");
        for (java.lang.reflect.Field field : Config.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    String valStr = p.getString(field.getName());
                    Object value = null;
                    if (field.getType() == boolean.class) {
                        value = Boolean.parseBoolean(valStr);
                    } else if (field.getType() == long.class) {
                        value = Long.parseLong(valStr);
                    } else if (field.getType() == int.class) {
                        value = Integer.parseInt(valStr);
                    }
                    field.set(null, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

//        dest = new File(p.getString("dest"));
//
//        long l;
//        try {
//            String str = p.getString("pageDelay");
//            l = Long.parseLong(str);
//        } catch (Exception e) {
//            l = 0;
//        }
//        pageDelay = l;
    }
        }
}
