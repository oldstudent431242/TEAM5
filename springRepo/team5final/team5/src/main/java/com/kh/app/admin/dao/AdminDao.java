package com.kh.app.admin.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.app.admin.vo.AdminVo;
import com.kh.app.adoption.vo.ApplyVo;
import com.kh.app.page.vo.PageVo;
import com.kh.app.survey.vo.SurveyVo;
import com.kh.app.visit.vo.VisitVo;
@Repository
public class AdminDao {

	public AdminVo login(SqlSessionTemplate sst, AdminVo vo) {
		return sst.selectOne("AdminMapper.login", vo);
	}

	public List<ApplyVo> getAdoptList(SqlSessionTemplate sst) {
		return sst.selectList("AdminMapper.getAdoptList");
	}

	public List<SurveyVo> getSurveyList(SqlSessionTemplate sst) {
		return sst.selectList("AdminMapper.getSurveyList");
		
	}

	public int approveAdoption(SqlSessionTemplate sst, ApplyVo vo) {
		return sst.update("ApplyMapper.approveAdoption", vo);
	}

	public int insertApproveDate(SqlSessionTemplate sst, ApplyVo vo) {
		return sst.update("ApplyMapper.insertApproveDate", vo);
	}

	public int rejectAdoption(SqlSessionTemplate sst, ApplyVo vo) {
		return sst.update("ApplyMapper.rejectAdoption", vo);
	}
	
	// 방문예약 목록조회
	public List<VisitVo> reservationList(SqlSessionTemplate sst, VisitVo vo) {
		return sst.selectList("AdminMapper.reservationList", vo);
	}
	// 전체 방문예약 수 조회
	public int selectVisitReservationCount(SqlSessionTemplate sst, String reservationStatus) {
		return sst.selectOne("AdminMapper.listCount", reservationStatus);
	}
	// 방문예약 상세 조회
	public VisitVo reservationDetail(SqlSessionTemplate sst, VisitVo vo) {
		return sst.selectOne("AdminMapper.reservationDetail", vo);
	}
	// 방문예약 상담취소
	public int adviceQuit(SqlSessionTemplate sst, VisitVo vo) {
		return sst.update("AdminMapper.quit", vo);
	}
	// 방문예약 상담완료
	public int adviceComplete(SqlSessionTemplate sst, VisitVo vo) {
		return sst.update("AdminMapper.complete", vo);
	}
	
}
