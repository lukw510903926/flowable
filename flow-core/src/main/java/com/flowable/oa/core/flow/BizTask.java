package com.flowable.oa.core.flow;

import com.flowable.oa.core.constants.HandleTypeEnum;
import lombok.Data;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

@Data
public class BizTask extends TaskEntityImpl {

    private static final long serialVersionUID = 5680270464693760532L;
    /**
     * 代办类型
     *
     * @see HandleTypeEnum
     */
    private Integer handleType;
}
