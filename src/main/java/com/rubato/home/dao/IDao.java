package com.rubato.home.dao;

import java.util.ArrayList;

import com.rubato.home.dto.FileDto;
import com.rubato.home.dto.RFBoardDto;
import com.rubato.home.dto.RMemberDto;
import com.rubato.home.dto.RReplyDto;

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
	
	//게시판 댓글
	public void rrwrite(String rrorinum, String rrid, String rrcontent);//insert 새 댓글 입력
	public ArrayList<RReplyDto> rrlist(String rrorinum);//해당 글의 댓글 리스트 select 댓글이 여러개니까 arraylist
	public void rrcount(String rrorinum);//update 댓글 등록시 해당글의 댓글갯수 1증가
	public void rrdelete(String rrnum);//댓글 삭제
	public void rrcountMinus(String rrorinum);// 댓글 삭제시 해달글의 댓글갯수 1감소
	
	//게시판 검색관련
	public ArrayList<RFBoardDto> rfbSearchTitleList(String searchKey);//select
	public ArrayList<RFBoardDto> rfbSearchContentList(String searchKey);//select
	public ArrayList<RFBoardDto> rfbSearchWriterList(String searchKey);//select
	
	//파일 업로드관련
	public void fileInfoInsert(int boardnum, String fileoriname, String filename, String fileextension, String fileurl);
	public ArrayList<RFBoardDto> boardLatestInfo(String rfbuserid);
	//현재 파일이 첨부되 글을 쓴 아이디로 검색된 글 목록
	public FileDto getFileInfo(String rfbnum);//select 파일이 첨부된 게시글의 번호로 조회한 첨부된 파일의 모든 정보 dto
	
}
