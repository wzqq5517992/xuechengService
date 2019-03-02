package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**课程计划
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-02 18:21
 */
@Mapper
public interface TeachplanMapper {
    /**
     * 课程计划查询
     * @param courseId
     * @return
     */
    public TeachplanNode selectList(String courseId);
}
