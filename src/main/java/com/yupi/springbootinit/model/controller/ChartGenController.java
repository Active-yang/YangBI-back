package com.yupi.springbootinit.model.controller;


import com.yupi.springbootinit.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author leikooo
 * @Description
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChartGenController implements Serializable {

    private static final long serialVersionUID = 847541708929254846L;

    /**
     * 图标名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 登录的用户
     */
    private User loginUser;

    public Long getLoginUserId() {
        return loginUser.getId();
    }
}
