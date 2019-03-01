package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-01 18:33
 */
public class CourseController  implements CourseControllerApi {
    /**
     * 查询课程计划列表
     * @param courseId
     * @return
     */
    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        return null;
    }

    /**
     * 添加课程计划列表
     * @param teachplan
     * @return
     */
    @Override
    public ResponseResult addTeachplan(Teachplan teachplan) {
        return null;
    }
}
