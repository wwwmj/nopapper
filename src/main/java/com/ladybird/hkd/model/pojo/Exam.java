package com.ladybird.hkd.model.pojo;

import com.ladybird.hkd.exception.BusinessException;

import java.sql.Time;
import java.util.Date;

/**
 * @author Shen
 * @description: 考试实体类
 * @create: 2019-03-20
 */
public class Exam {
    private String exam_id;     //考试号
    private Integer course;     //考试课程
    private Integer grade;      //考试班级
    private Date pre_time;      //预设开始时间
    private Date begin_time;    //考试开始时间
    private Integer duration;   //考试时长  （毫秒）
    private Integer state;      //状态 默认0 表示考试未开始

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Date getPre_time() {
        return pre_time;
    }

    public void setPre_time(Date pre_time) {
        this.pre_time = pre_time;
    }

    public Date getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}