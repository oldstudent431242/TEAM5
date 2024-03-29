package com.kh.app.report.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.app.page.vo.PageVo;
import com.kh.app.report.service.ReportService;
import com.kh.app.report.vo.ReportVo;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

	private final ReportService service;
	private final HttpServletRequest req;
	
	
	// 제보 목록 조회
	@GetMapping("list")
	public Map<String, Object> list(@RequestParam(value="pno", 
		    required = false, defaultValue="1")String pno) {
		
		// 전체 게시글 수 조회
		int listCount = service.selectBoardCount();
		
		String currentPage_ = pno; // pno 받아와야함
		if(currentPage_ == null) {
			currentPage_ = "1";
		}
		int currentPage = Integer.parseInt(currentPage_);	// 현재 페이지 (화면에서 전달받기)
		int pageLimit = 5;									// 페이징 영역 페이지 갯수 (페이지를 몇개씩 띄울껀지)
		int boatdLimit = 8;									// 한 페이지에 보여줄 게시글 갯수
		PageVo pvo = new PageVo(listCount, currentPage, pageLimit, boatdLimit);
		
		
		// 목록 조회
		List<ReportVo> voList = service.list(pvo);
		Map<String, Object> map = new HashMap<>();
		if (currentPage_ == null && voList == null) {
			map.put("msg", "fail");
		} else {
			map.put("msg", "success");
			map.put("voList", voList);
			map.put("pvo", pvo);
		}
		return map;
	}
	
	
	// 제보 상세 조회
	@PostMapping("detail")
	public Map<String, Object> detail(@RequestBody ReportVo vo) {
		
		ReportVo dbVo = service.detail(vo);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("dbVo", dbVo);
		if (dbVo == null) {
			map.put("msg", "fail");
		} else {
			map.put("msg", "success");
		}
		return map;
	}
	
	
	
	// 제보 작성
	@PostMapping("write")
	public Map<String, String> write(ReportVo vo, MultipartFile f) throws Exception {
		
		// 이미지 업로드
		String imagePath = saveFile(f);
		ReportVo imgVo = new ReportVo();
		imgVo.setImagePath(imagePath);
		int resultImg = service.insert(imgVo);
		
		// 이미지 시퀀스넘버 조회
		vo.setImageNo(service.selectImageSeqNo());

		// 게시글 작성
		Map<String, String> map = new HashMap<String, String>();
		int resultReport = service.write(vo);
		
		if (resultImg == 1 ) {
			map.put("ImgMsg", "img insert success");
			if (resultReport == 1 ) {
				map.put("ReportMsg", "board write success");
			} else {
				map.put("ReportMsg", "board write fail");
			}
		} else {
			map.put("ImgMsg", "img insert fail");
		}
		
		return map;
	}
	// 제보 수정
	@PostMapping("edit")
	public Map<String, String> edit(ReportVo vo, MultipartFile f) throws Exception {
		
		// 이미지 업데이트
		String imagePath = saveFile(f);
		ReportVo imgVo = new ReportVo();
		imgVo.setImagePath(imagePath);
		imgVo.setImageNo(vo.getImageNo());
		int resultImg = service.editImage(imgVo);
		
		// 게시글 업데이트
		Map<String, String> map = new HashMap<String, String>();
		int resultReport = service.editBoard(vo);
		
		if (resultImg == 1) {
			map.put("ImgMsg", "img update success");
			if (resultReport == 1 ) {
				map.put("ReportMsg", "board update success");
			} else {
				map.put("ReportMsg", "board update fail");
			}
		} else {
			map.put("ImgMsg", "img update fail");
		}
		
		return map;
	}
	/**
	 * 파일을 서버에 저장하고, 파일 전체 경로를 리턴함
	 * 
	 * @param 파일객체
	 * @param 파일경로
	 * @return 실제파일저장경로(파일경로+파일명)
	 * @throws Exception 
	 */
	private String saveFile(MultipartFile f) throws Exception {
		
        String myUrl = req.getServletContext().getRealPath("/");
        int lastIndex = myUrl.lastIndexOf("\\");
        int lastIndexBeforeTarget = myUrl.lastIndexOf("target");
        String changeUrl = myUrl.substring(lastIndexBeforeTarget, lastIndex);
        
        String str = "";
        if (lastIndexBeforeTarget != -1) {
            str = myUrl.replace(changeUrl
            		, "src\\main\\webapp\\resources\\upload\\img\\");
        }
       
        
        String path = str;
        
        String originName = f.getOriginalFilename();
        
        // 원래는 "path + changeName(랜덤값) + 확장자" 로 해야함 
        File target = new File(path + originName); 
        
        // 파일 바이트코드 읽어서 타겟에 저장
        f.transferTo(target);
        
        return path + originName;
	} //method
	
	
	// 제보 삭제
	@DeleteMapping
	public Map<String, String> delete(@RequestBody ReportVo vo) {
		
		Map<String, String> map = new HashMap<String, String>();
		int result = service.delete(vo);
		
		if (result == 1) {
			map.put("msg", "success");
		} else {
			map.put("msg", "fail");
		}
		
		return map;
	}
	
} // class
