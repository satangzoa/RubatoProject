package com.rubato.home.dao;

import java.util.ArrayList;

import com.rubato.home.dto.RFBoardDto;
import com.rubato.home.dto.RMemberDto;

public interface IDao {
	//멤버관련
	public void joinMember(String mid, String mpw, String mname, String memail);//회원가입이니까 반환타입이 없다. insert
	public  int checkUserId(String mid); // xml에서 select문 int니까 반환 타입 int
	public int checkUserIdAndPw(String mid, String mpw);
	
	//게시판 관련
	public void rfbwrite(String rfbname, String rfbtitle, String rfbcontent, String rfbid);
	public ArrayList<RFBoardDto> rfblist(); // 게시판 리스트 select 다가져와야해서 매개변수는 필요없음
	public int rfboardAllCount();// 모든 게시물의 갯수 가져온다.매개 변수는 필요없음. 총게시물 갯수. select
	public RFBoardDto rfboardView(String rfbnum); //select 클릭한 글의 게시물 내용 보기
	public void delete(String rfbnum);//글삭제 delete
	public void rfbhit(String rfbnum);//조회수 update
}
