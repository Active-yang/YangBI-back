package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AIManager;
import com.yupi.springbootinit.mapper.ChartMapper;
import com.yupi.springbootinit.model.controller.ChartGenController;
import com.yupi.springbootinit.model.dto.chart.ChartGenResult;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.vo.BiResponse;
import com.yupi.springbootinit.service.ChartService;

import com.yupi.springbootinit.utils.ChartDataUtil;
import com.yupi.springbootinit.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
* @author 33061
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-01-30 17:03:58
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {


    @Resource
    private AIManager aiManager;

    @Resource
    private ChartMapper chartMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BiResponse getChart(final MultipartFile multipartFile,
                               final ChartGenController chartGenController) {
        ThrowUtils.throwIf(chartGenController == null, ErrorCode.PARAMS_ERROR);
        final String goal = chartGenController.getGoal();
        final String chartType = chartGenController.getChartType();
        // 分析 xlsx 文件
        String cvsData = ExcelUtils.getExcelFileName(multipartFile);
        // 发送给 AI 分析数据
        ChartGenResult chartGenResult = ChartDataUtil.getGenResult(aiManager, goal, cvsData, chartType);
        //更具AI返回的内容，提取有效的部分保存到数据库中，
        System.out.println("------------------------------------------------");
        String genChart = chartGenResult.getGenChart();
        String genResult = chartGenResult.getGenResult();
        System.out.println("------------------------------------------------");
        System.out.println(genChart);
        System.out.println(genResult);
        Chart chart = new Chart(chartGenController.getName(), goal, chartType, genChart, genResult, chartGenController.getLoginUserId());
        boolean saveResult = this.save(chart);
        Long charId = chart.getId();
        // 创建表、保存数据
        saveCVSData(cvsData, charId);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "保存图表信息失败");
        return new BiResponse(charId, genChart, genResult);
    }


    /**
     * 生成建表格 SQL 并且插入 cvs 数据到数据库
     *
     * @param cvsData
     * @param chartId
     */
    private void saveCVSData(final String cvsData, final Long chartId) {
        String[] columnHeaders = cvsData.split("\n")[0].split(",");
        StringBuilder sqlColumns = new StringBuilder();
        for (int i = 0; i < columnHeaders.length; i++) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(columnHeaders[i]), ErrorCode.PARAMS_ERROR);
            sqlColumns.append("`").append(columnHeaders[i]).append("`").append(" varchar(50) NOT NULL");
            if (i != columnHeaders.length - 1) {
                sqlColumns.append(", ");
            }
        }
        String sql = String.format("CREATE TABLE charts_%d ( %s )", chartId, sqlColumns);
        String[] columns = cvsData.split("\n");
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("INSERT INTO charts_").append(chartId).append(" VALUES ");
        for (int i = 1; i < columns.length; i++) {
            String[] strings = columns[i].split(",");
            insertSql.append("(");
            for (int j = 0; j < strings.length; j++) {
                insertSql.append("'").append(strings[j]).append("'");
                if (j != strings.length - 1) {
                    insertSql.append(", ");
                }
            }
            insertSql.append(")");
            if (i != columns.length - 1) {
                insertSql.append(", ");
            }
        }
        try {
            chartMapper.createTable(sql);
            chartMapper.insertValue(insertSql.toString());
        } catch (Exception e) {
            log.error("插入数据报错 " + e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}




