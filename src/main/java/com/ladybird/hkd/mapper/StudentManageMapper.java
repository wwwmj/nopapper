package com.ladybird.hkd.mapper;

import com.ladybird.hkd.model.pojo.Grade;
import com.ladybird.hkd.model.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface StudentManageMapper {
    List<Integer> selectAllGrade(@Param("gradelow") int gradelow) throws Exception;

    List<Student> selectStudent(Student student) throws Exception;

    int addStudent(Student student) throws Exception;

    int deleteStudent(String stu_num) throws Exception;

    int updateStudent(Student student) throws Exception;

    Student findStudent(Student student) throws Exception;
}
