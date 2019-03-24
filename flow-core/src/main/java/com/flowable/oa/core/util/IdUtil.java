package com.flowable.oa.core.util;

import java.util.UUID;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/24 11:02
 **/
public class IdUtil {

    public static String uuid(){

        return UUID.randomUUID().toString().replace("-","");
    }
}
