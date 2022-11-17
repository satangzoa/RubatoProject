package com.rubato.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rubato.home.dao.IDao;

@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "board_list")
	public String board_list() {
		return "board_list";
	}
	
	@RequestMapping(value = "board_view")
	public String board_view() {
		return "board_view";
	}
	
	@RequestMapping(value = "board_write")
	public String board_write() {
		return "board_write";
	}

	@RequestMapping(value = "member_join")
	public String member_join() {
		return "member_join";
	}
	
	@RequestMapping(value = "joinOk")
	public String joinOk(HttpServletRequest request) {
		String memberId = request.getParameter("mid");//"mid" 파라미터의 이름만 넣어준다
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		IDao dao = sqlSession.getMapper(IDao.class);//IDao.java에서 class 불러온다
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);//맵핑된 메소드가 insert 니까 반환타입이 없다.
		
		return "redirect:index";
	}
	
	@RequestMapping(value = "loginOk")
	public String loginOk(HttpServletRequest request, HttpSession session) {
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		int checkIdFlag = dao.checkUserIdAndPw(memberId, memberPw);//1이면 로그인 되고 0이면 로그인 안된다.
		
		if(checkIdFlag == 1) {
			session.setAttribute("memberId", memberId);//로그인 하겠다는 뜻
		}
		return "redirect:index";
	}
}








