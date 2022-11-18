package com.rubato.home.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rubato.home.dao.IDao;
import com.rubato.home.dto.RFBoardDto;
import com.rubato.home.dto.RReplyDto;

@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "board_list")
	public String board_list(Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		ArrayList<RFBoardDto> boardDtos = dao.rfblist();
		
		int boardCount = dao.rfboardAllCount();
		model.addAttribute("boardList",boardDtos);
		model.addAttribute("boardCount",boardCount);
		
		
		
		return "board_list";
	}
	
	@RequestMapping(value = "board_view")
	public String board_view(HttpServletRequest request,Model model) {
		
		String rfbnum = request.getParameter("rfbnum");
		//사용자가 글리스트에서 클릭한 글의 번호
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.rfbhit(rfbnum); // 조회수 증가
		RFBoardDto rfboardDto = dao.rfboardView(rfbnum);
		ArrayList<RReplyDto> replyDtos = dao.rrlist(rfbnum);
		
		model.addAttribute("rfbView",rfboardDto);
		model.addAttribute("replylist",replyDtos);
		
		return "board_view";
	}
	
	@RequestMapping(value = "board_write")
	public String board_write(HttpSession session, HttpServletResponse response) {
		String sessionId = (String) session.getAttribute("memberId");
		if(sessionId == null) {//침이면 로그인이 안된 상태
			PrintWriter out;
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			out.println("<script>alert('로그인하지 않으면 글을 쓰실 수 없습니다!');history.go(-1);</script>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		return "board_write";
	}

	@RequestMapping(value = "member_join")
	public String member_join() {
		return "member_join";
	}
	
	@RequestMapping(value = "joinOk")
	public String joinOk(HttpServletRequest request, HttpSession session) {
		String memberId = request.getParameter("mid");//"mid" 파라미터의 이름만 넣어준다
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		IDao dao = sqlSession.getMapper(IDao.class);//IDao.java에서 class 불러온다
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);//맵핑된 메소드가 insert 니까 반환타입이 없다.
		session.setAttribute("memberId", memberId);//가입과 동시에 로그인
		return "redirect:index";
	}
	
	
	
	
	@RequestMapping(value = "loginOk")
	public String loginOk(HttpServletRequest request, HttpSession session) {
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		int checkIdFlag = dao.checkUserIdAndPw(memberId, memberPw);//1이면 로그인 되고 0이면 로그인 안된다.
		
		if(checkIdFlag == 1) {//참이면 로그인 성공
			session.setAttribute("memberId", memberId);//로그인 하겠다는 뜻
		}
		return "redirect:index";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		
		session.invalidate();//세션 삭제하기
		return "redirect:index";
	}
	
	@RequestMapping("writeOk")
	public String writeOk(HttpServletRequest request,HttpSession session) {
		
		
		String name = request.getParameter("rfbname");
		String title = request.getParameter("rfbtitle");
		String content = request.getParameter("rfbcontent");
		
		String sessionId = (String) session.getAttribute("memberId");
		//글쓴이의 아이디는 현재 로그인된 유저의 아이디이므로 세션에서 가져와서 전달
		IDao dao = sqlSession.getMapper(IDao.class);
		dao.rfbwrite(name, title, content, sessionId);
		
		return "redirect:board_list";
	}
	
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request) {
		
		String rfbnum = request.getParameter("rfbnum");
		IDao dao = sqlSession.getMapper(IDao.class);
		dao.delete(rfbnum);
		
		
		return "redirect:board_list";
	}
	
	@RequestMapping(value = "replyOk")
	public String replyOk(HttpServletRequest request, HttpSession session, Model model, HttpServletResponse response) {
		
		String rrorinum = request.getParameter("rfbnum");//댓글이 달린 원글 번호
		String rrconetent = request.getParameter("rrcontent");//댓글 내용
	
		String sessionId = (String) session.getAttribute("memberId");//현재 로그인한 유저의 아이디
		
		if(sessionId == null) {//참이면 로그인이 안된 상태
			PrintWriter out;
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			out.println("<script>alert('로그인하지 않으면 글을 쓰실 수 없습니다!');history.go(-1);</script>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}else {
			IDao dao = sqlSession.getMapper(IDao.class);
			dao.rrwrite(rrorinum,sessionId, rrconetent );//댓글쓰기
			dao.rrcount(rrorinum);//해당글의 댓글 총 개수 증가
			
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			ArrayList<RReplyDto> replyDtos = dao.rrlist(rrorinum);
			
			model.addAttribute("rfbView", rfboardDto); //원글의 게시글 내용 전부
			model.addAttribute("replylist", replyDtos); //해당 글에 달린 댓글 리스트
		
		}
		
			return "board_view";//해당글 아래 댓글이 보여야해서 board_view를 요청해야한다
	}
	
	@RequestMapping(value = "replyDelete")
	public String replyDelete(HttpServletRequest request, Model model) {
		
		String rrnum = request.getParameter("rrnum");//댓글 고유번호
		String rrorinum = request.getParameter("rfbnum");//댓글이 달린 원글의 고유번호
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.rrdelete(rrnum);//댓글 삭제
		dao.rrcountMinus(rrorinum);//해당 글의 댓글 갯수 1감소
		
		RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
		ArrayList<RReplyDto> replyDtos =  dao.rrlist(rrorinum);
		
		model.addAttribute("rfbView", rfboardDto);//원글의 게시글 내용 전부
		model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
		
		return "board_view";
	}
	
	@RequestMapping("search_list")
	public String search_list() {
		
		
		return "board_list";
	}
	
}










