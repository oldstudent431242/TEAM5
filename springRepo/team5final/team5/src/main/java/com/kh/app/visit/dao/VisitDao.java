package com.kh.app.visit.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.app.visit.vo.VisitVo;

@Repository
public class VisitDao {

	// 특정일 예약 목록 조회
	public List<VisitVo> list(SqlSessionTemplate sst, String reservationDate) {
		return sst.selectList("VisitMapper.list", reservationDate);
	}
	
	// 방문예약 중복 확인
	public VisitVo check(SqlSessionTemplate sst, String memberNo) {
		return sst.selectOne("VisitMapper.check", memberNo);
	} 

	// 예약 상세 조회
	public VisitVo detail(SqlSessionTemplate sst, String memberNo) {
		return sst.selectOne("VisitMapper.detail", memberNo);
	} 
	
	// 방문 예약하기
	public int insert(SqlSessionTemplate sst, VisitVo vo) {
		return sst.insert("VisitMapper.insert", vo);
	}
	
	// 예약 변경
	public int edit(SqlSessionTemplate sst, VisitVo vo) {
		return sst.update("VisitMapper.edit", vo);
	}
	
	// 예약 취소->삭제
	public int quit(SqlSessionTemplate sst, VisitVo vo) {
		return sst.update("VisitMapper.quit", vo);
	}
	
	// 상담 완로->삭제
	public int complete(SqlSessionTemplate sst, VisitVo vo) {
		return sst.update("VisitMapper.complete", vo);
	}

	
} // class
