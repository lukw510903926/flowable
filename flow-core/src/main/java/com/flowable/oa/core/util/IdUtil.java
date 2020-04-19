package com.flowable.oa.core.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/24 11:02
 **/
public class IdUtil {

    public static String uuid() {

        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成26+序列号 时间戳 + 6位随机数 + 4位随机字符
     *
     * @return
     */
    public static String generate(String prefix) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssS"));
        prefix = StringUtils.isBlank(prefix) ? "T" : prefix;
        String sn = prefix + now + ThreadLocalRandom.current().nextInt(100000, 999999);
        return StringUtils.rightPad(sn, 26, genRandomNumber(4));
    }

    public static String genRandomNumber(int count) {

        StringBuilder sb = new StringBuilder();
        String str = "123456789QWERTYUPKJHGFDSAZXCVBNM";
        for (int i = 0; i < count; i++) {
            int num = RandomUtils.nextInt(0, str.length());
            if (i == 0) {
                num = RandomUtils.nextInt(0, 9);
            }
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num) + ""), "");
        }
        return sb.toString();
    }
}
