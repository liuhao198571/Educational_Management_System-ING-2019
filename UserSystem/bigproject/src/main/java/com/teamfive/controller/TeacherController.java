package com.teamfive.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.teamfive.pojo.PageInfo;
import com.teamfive.pojo.Student;
import com.teamfive.pojo.Teacher;
import com.teamfive.service.TeacherService;

@Controller
@RequestMapping("/teacher")
@SessionAttributes(value= {"teacher","pInfo"})
public class TeacherController {
	@Autowired
	TeacherService teacherService;

	// 跳转登录
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// 登录验证
	@RequestMapping("/checkLogin")
	public String checkLoing(Teacher teacher, Model model) {
		teacher = teacherService.checkLogin(teacher.getT_username(), teacher.getT_password());
		System.out.println(teacher);
		if (teacher != null) {
			if (teacher.getGid() == 1) {
				model.addAttribute("teacher", teacher);
				return "success";
			} else if (teacher.getGid() == 2 || teacher.getGid() == 3) {
				model.addAttribute("teacher", teacher);
				return "success2";
			}
		}
		return "login";
	}

	// 登出
	@RequestMapping("/outLogin")
	public String outLogin(HttpSession session, SessionStatus sessionStatus) {
		session.removeAttribute("teacher");
		session.invalidate();
		sessionStatus.setComplete();
		return "login";
	}

	// 跳转学生注册
	@RequestMapping("/registS")
	public String registS() {
		return "registS";
	}

	// 执行学生注册
	@RequestMapping("/doRegistS")
	public String doRegist(Student student) {
		System.out.println(student);
		try {
			teacherService.RegistS(student);
			return "success_r";
		} catch (Exception e) {
			return "error";
		}
	}

	// 跳转教师注册
	@RequestMapping("/registT")
	public String registT(Teacher teacher) {
		if (teacher.getGid() >= 3) {
			return "error";
		}
		return "registT";
	}

	// 执行教师注册
	@RequestMapping("/doRegistT")
	public String doRegist(Teacher teacher) {
		System.out.println(teacher);
		try {
			teacherService.RegistT(teacher);
			return "success_r";
		} catch (Exception e) {
			return "error";
		}
	}

	// 修改信息
	@RequestMapping("/updateT")
	public String teacherUpdate() {
		return "updateT";
	}

	// 执行修改
	@RequestMapping("/doUpdate")
	public String doUpdate(Teacher teacher) {
		System.out.println("当前用户：" + teacher.getT_name());
		try {
			teacherService.updateTeac(teacher);
			System.out.println("success!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return "success2";
		} catch (Exception e) {
			return "error";
		}
	}

	// 修改密码
	@RequestMapping("/updateTPW")
	public String updateTPW() {
		return "updateTPW";
	}

	// 修改密码执行
	@RequestMapping("/doUpdatePW")
	public String doUpdatePW(Teacher teacher) {
		System.out.println("当前用户：" + teacher.getT_name());
		try {
			teacherService.updateTPW(teacher);
			System.out.println("success!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return "updateT";
		} catch (Exception e) {
			return "error";
		}
	}

	//分页操作
	@RequestMapping("/showpage")
	public String selByPage(Model model, HttpServletRequest request) {
		String s_name = request.getParameter("s_name");
		String t_name = request.getParameter("t_name");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		
		PageInfo pInfo = teacherService.showPage(s_name, t_name, pageSize, pageNumber);
		
		model.addAttribute("pInfo", pInfo);
		
		
		return "showpage";

	}
	
	//查询课程成绩
	@RequestMapping("/checkScore/{cid}")
	public ModelAndView checkScore(@PathVariable("cid") Integer cid, ModelAndView mv){
		List<Course> courseList = teacherService.courseList(cid);
		mv.addObject("checkScore", courseList);
		mv.setViewName("checkscore");
		System.out.println(courseList);
		return mv;
	}

}
