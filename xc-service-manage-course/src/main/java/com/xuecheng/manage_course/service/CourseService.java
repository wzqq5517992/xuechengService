package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 课程计划
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-02 19:21
 */
@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;

    /**
     * 课程计划查询
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId){
        return  teachplanMapper.selectList(courseId);
    }

}
