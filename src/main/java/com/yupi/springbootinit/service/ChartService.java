package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.controller.ChartGenController;
import com.yupi.springbootinit.model.dto.chart.GenChartByAiRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author 33061
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-01-30 17:03:58
*/
public interface ChartService extends IService<Chart> {
    BiResponse getChart(final MultipartFile multipartFile, final ChartGenController chartGenController);

}
