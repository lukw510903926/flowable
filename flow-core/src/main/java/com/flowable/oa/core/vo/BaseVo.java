package com.flowable.oa.core.vo;

import com.github.pagehelper.IPage;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:48
 **/
@Data
public class BaseVo implements IPage, Serializable {

    private static final long serialVersionUID = 8166703752410762351L;
    private Integer pageNum;

    private Integer pageSize;

    private String orderBy;
}
