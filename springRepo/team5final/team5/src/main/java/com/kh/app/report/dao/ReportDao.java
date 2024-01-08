package com.kh.app.report.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.app.report.vo.ReportVo;

@Repository
public class ReportDao {

	
	// 게시글 목록 조회
	public List<ReportVo> list(SqlSessionTemplate sst) {
		return sst.selectList("ReportMapper.list");
	}

	// 게시글 상세 조회
	
	
	// 게시글 작성
	public int insert(SqlSessionTemplate sst, ReportVo vo) {
		return sst.insert("ReportMapper.insert", vo);
	}
		// 로그인 멤버의 프라이머리 키
		// 이미지의 프라이머리 키
	
	
	// 게시글 수정
	
	
	// 게시글 삭제

} // class