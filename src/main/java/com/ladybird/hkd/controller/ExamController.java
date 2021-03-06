package com.ladybird.hkd.controller;

import com.ladybird.hkd.annotation.CheckToken;
import com.ladybird.hkd.exception.BusinessException;
import com.ladybird.hkd.exception.ParamException;
import com.ladybird.hkd.model.example.PaperEditExample;
import com.ladybird.hkd.model.example.ExamExample;
import com.ladybird.hkd.model.json.ResultJson;
import com.ladybird.hkd.model.pojo.*;
import com.ladybird.hkd.service.ExamService;
import com.ladybird.hkd.service.StudentService;
import com.ladybird.hkd.service.TeacherService;
import com.ladybird.hkd.util.ConstConfig;
import com.ladybird.hkd.util.JsonUtil;
import com.ladybird.hkd.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Shen
 * @description: 考试管理
 * @create: 2019-03-10
 */
@Api("考试管理类")
@CrossOrigin
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController{
    @Autowired
    private ExamService examService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;



    @CheckToken
    @ApiOperation(value = "修改试卷",notes= "修改试卷题型分配")
    @ResponseBody
    @RequestMapping(value = "/editPaper",method = {RequestMethod.POST}, consumes="application/json", produces="application/json")
    public Object editPaper(@RequestBody PaperEdit paperEdit) throws Exception{
        PaperEdit.checkParams(paperEdit);
        examService.updatePaper(paperEdit);
        return ResultJson.Success(paperEdit);
    }

    @ApiOperation("请求试卷当前配置信息")
    @CheckToken
    @RequestMapping(value = "/checkOutPaper",method = RequestMethod.GET)
    @ResponseBody
    public Object checkOutPaper() throws Exception{
        List<PaperEditExample> paperEdits = examService.checkOutPaper();
        return ResultJson.Success(paperEdits);
    }



    @ApiOperation("提交试卷成绩")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "score",value = "分数",required = true),
            @ApiImplicitParam(name = "exam",value = "考试场次",required = true)
    })
    @CheckToken
    @RequestMapping(value = "/commitPaper",method = RequestMethod.GET)
    @ResponseBody
    public Object commitPaper(String score,String exam,NativeWebRequest request) throws Exception{
        if (ParamUtils.stringIsNull(score) || ParamUtils.stringIsNull(exam)){
            throw new ParamException("分数没传！");
        }
        String studentJson = (String) request.getAttribute(ConstConfig.CURRENT_OBJECT, RequestAttributes.SCOPE_REQUEST);
        if (ParamUtils.stringIsNull(studentJson)) {
            throw new BusinessException("没有学生信息！");
        }
        Student student = JsonUtil.jsonToPojo(studentJson, Student.class);
        if (student == null) {
            throw new BusinessException("没有学生信息！");
        }
        Score param = new Score(student.getStu_num(), exam, new BigDecimal(score));
        studentService.checkInScore(param);

        return ResultJson.Success();
    }

    //微信端请求单个试卷配置
    @ResponseBody
    @RequestMapping(value = "/paper")
    public Object paper(String exam) throws Exception {
        if (exam == null || "".equals(exam.trim()))
            throw new ParamException("考试场次未知！");
        return ResultJson.Success(examService.checkOutPaperByCourse(exam));
    }

    @ApiOperation("学生请求考试安排")
    @ApiImplicitParam(name = ConstConfig.AUTHORIZATION,value = "token:xxx accessToken")
    @CheckToken
    @RequestMapping(value = "/exam",method = RequestMethod.GET)
    @ResponseBody
    public Object exam(NativeWebRequest request) throws Exception{
        String studentJson = (String) request.getAttribute(ConstConfig.CURRENT_OBJECT, RequestAttributes.SCOPE_REQUEST);
        if (ParamUtils.stringIsNull(studentJson)) {
            return ResultJson.ServerException();
        }
        Student student = JsonUtil.jsonToPojo(studentJson, Student.class);
        if (student == null) {
            throw new BusinessException("Token中没有用户信息！");
        }
        if (ParamUtils.stringIsNull(student.getStu_num()))
            return ResultJson.ServerException();
        List<ExamExample> examExamples = examService.selectExamByStu(student);
        if (examExamples.size() == 0)
            return ResultJson.BusinessErrorException("暂时没有考试！",null);
        return examExamples;
    }

}