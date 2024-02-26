package com.yupi.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.springbootinit.model.entity.Chart;

import java.util.List;
import java.util.Map;

/**
* @author 33061
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-01-30 17:03:58
* @Entity com.yupi.springbootinit.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    /**
     * 动态的创建数据库
     * @param creatTableSQL
     */
    void createTable(final String creatTableSQL);

    /**
     * 向动态创建的数据库之中插入数据
     *
     * @param insertCVSData
     * @return
     */
    void insertValue(final String insertCVSData);

}




