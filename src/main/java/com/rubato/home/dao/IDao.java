package com.rubato.home.dao;

import com.rubato.home.dto.RMemberDto;

public interface IDao {
	//멤버관련
	public void joinMember(String mid, String mpw, String mname, String memail);//회원가입이니까 반환타입이 없다. insert
	public  int checkUserId(String mid);//xml에서 select문 int니까 반환 타입 int
	public int checkUserIdAndPw(String mid, String mpw);
	
	//게시판 관련
	public void rfbwrite(String rfbname, String rfbtitle, String rfbcontent, String rfbid);
	
	
	
}
