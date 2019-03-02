package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**Teachplan数据层
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-03 0:37
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    //SELECT * FROM teachplan WHERE courseid = '297e7c7c62b888f00162b8a7dec20000' AND parentid='0'
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}