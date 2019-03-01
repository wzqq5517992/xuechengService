package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-01 18:28
 */
@Api(value="课程管理接口",description = "课程管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
     TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
     ResponseResult addTeachplan(Teachplan teachplan);
}
