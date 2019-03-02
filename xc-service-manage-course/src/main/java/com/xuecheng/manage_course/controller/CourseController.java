package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-01 18:33
 */
@RestController
@RequestMapping("/course")
public class CourseController  implements CourseControllerApi {
    @Autowired
    CourseService courseService;
    /**
     * 查询课程计划列表
     * @param courseId
     * @return
     */
    @GetMapping("/teachplan/list/{courseId}")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {

        return courseService.findTeachplanList(courseId);
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
