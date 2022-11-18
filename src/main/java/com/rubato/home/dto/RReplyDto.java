package com.rubato.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RReplyDto {
	
	private int rrnum;
	private String rrcontent;
	private String rrid;
	private int rrorinum; // 댓글이 달린 원글의 게시판번호
	private String rrdate;
}
