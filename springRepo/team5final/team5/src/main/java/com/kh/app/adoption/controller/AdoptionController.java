package com.kh.app.adoption.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.app.adoption.service.AdoptionService;
import com.kh.app.adoption.vo.AdoptionVo;
import com.kh.app.page.vo.PageVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("adoption")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdoptionController {

	private final AdoptionService service;
	private final HttpServletRequest req;
	
	// 입양신청 목록 조회
	@GetMapping("list")
	public Map<String, Object> list(@RequestParam(value="pno", required=false, defaultValue="1")String pno) {

		//전체 게시글 갯수 조회
		int listCount = service.selectBoardCount();
		
		System.out.println(pno);
		
		String currentPage_ = pno;							//pno 받아오기. "1" 안됨 변수로!
		if(currentPage_ == null) {
			currentPage_ = "1";
		}
		int currentPage = Integer.parseInt(currentPage_);	//현재 페이지 (화면에서 전달받기)
		int pageLimit = 5;									// 페이징 영역 페이지 갯수 (페이지를 몇개씩 띄울껀지)
		int boardLimit = 9;									// 한 페이지에 보여줄 게시글 갯수
		PageVo pvo = new PageVo(listCount, currentPage, pageLimit, boardLimit);
		System.out.println("전체 게시글 갯수 조회 pvo : " + pvo);
		
		
		//목록조회
		List<AdoptionVo> voList = service.list(pvo);
		System.out.println("목록조회 voList : " + voList);
		
		Map<String, Object> map = new HashMap<>();
		if (currentPage_ == null && voList == null) {
			map.put("msg", "bad");
		} else {
			map.put("msg", "good");
			map.put("voList", voList);
			map.put("pvo", pvo);
		}
		
		return map;
	}
	
	// 입양신청 상세 조회
	@PostMapping("detail")
	public Map<String, Object> detail(@RequestBody AdoptionVo vo) {
		
		System.out.println("입양신청 상세 조회 vo : " + vo);
		
		AdoptionVo dbVo = service.detail(vo);
		
		System.out.println("입양신청 상세 조회 dbvo : " + dbVo );
		
		Map<String, Object> map = new HashMap<>();
		map.put("dbVo", dbVo);
		if (dbVo == null) {
			map.put("msg", "good");
		} else {
			map.put("msg", "bad");
		}
		
		return map;
	}
	
	// 입양신청 작성
	@PostMapping("write")
	public Map<String, String> write(AdoptionVo vo, MultipartFile file) throws Exception {
		
		System.out.println("vo : " + vo);
		
		//이미지 업로드 (1)
		String imagePath = saveFile(file);
		AdoptionVo imgVo = new AdoptionVo();
		imgVo.setImagePath(imagePath);
		int resultImg = service.insert(imgVo);
		
		//이미지 시퀀스넘버 조회 (2)
		vo.setImageNo(service.selectImageSeqNo());
		
		
		Map<String, String> map = new HashMap<String, String>();
		
		//vo를 통해서 RESCUE_DOG 추가
		int insertRescueDogResult = service.insertRescueDog(vo);
		if(insertRescueDogResult != 1){
			throw new Exception();
		}
		
		// 견종을 기반으로 RESCUE_DOG_NO 조회 (3)
		String dogNo = service.findDogNoBtBreed(vo.getBreed());
		System.out.println("dogNo : " + dogNo);
		// 견종 기반 유기견NO 조회
		if (dogNo != null) {
		    vo.setRescueDogNo(dogNo);
		}
		
		//게시글 작성 (4)
		int resultBoard = service.write(vo);
		System.out.println("게시글 작성 실패시 : " + resultBoard);

		//이미지 작성 성공 여부
		if (resultImg == 1) {
			map.put("imgMsg", "img insert good");
			System.out.println("이미지 업로드 성공");
		} else {
			map.put("imgMsg", "img insert bad");
			System.out.println("이미지 업로드 실패");
		}
		
		// 견종 기반 유기견NO 조회
		if (dogNo != null) {
			map.put("dogMsg", "dog insert good");
		} else {
			map.put("dogMsg", "dog insert bad");
		}
		
		// 게시글 작성 성공 여부
		map.put("imagePath", imagePath);
		System.out.println(imagePath);
		if (resultBoard == 1) {
			map.put("boardMsg", "board write good");
			System.out.println("게시글 작성 성공");
		} else {
			map.put("boardMsg", "board write bad");
			System.out.println("게시글 작성 실패");
		}
		// 트랜잭션처리(isfatching)
				
		return map;
	}

	  // 입양신청 수정
	   @PostMapping("edit")
	   public Map<String, String> edit(AdoptionVo vo, MultipartFile file) throws Exception {
	      
	      // 이미지 업데이트
	      String imagePath = saveFile(file);
	      AdoptionVo imgVo = new AdoptionVo();
	      imgVo.setImagePath(imagePath);
	      imgVo.setImageNo(vo.getImageNo());
	      int resultImg = service.editImage(imgVo);
	      
	      // 게시글 업데이트
	      Map<String, String> map = new HashMap<String, String>();
	      int resultBoard = service.editBoard(vo);
	      
	      if (resultImg == 1) {
	         map.put("imgMsg", "img update good");
	         System.out.println("이미지 수정 성공 !");
	         if (resultBoard == 1) {
	            map.put("boardMsg", "board update good");
	            System.out.println("게시글 수정 성공 !");
	         } else {
	            map.put("boardMsg", "board update bad");
	            System.out.println("게시글 수정 실패");
	         }
	      } else {
	         map.put("imgMsg", "img update bad");
	         System.out.println("게시글 수정 실패");
	      }
	      
	      return map;
	   }
	

	/**
	 * 파일을 서버에 저장하고, 파일 전체 경로를 리턴함
	 * @param 파일객경로
	 * @param 파일객체
	 * @return 실제파일저장경로(파일경로 + 파일명)
	 */
	private String saveFile(MultipartFile file) throws Exception {
		
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
        
        String originName = file.getOriginalFilename();
        
        // 원래는 "path + changeName(랜덤값) + 확장자" 로 해야함 
        File target = new File(path + originName); 
        
        // 파일 바이트코드 읽어서 타겟에 저장
        file.transferTo(target);
        
        return path + originName;
	}
	
	// 입양완료 처리
    @PostMapping("complete")
    public Map<String, String> completeAdoption(@RequestBody AdoptionVo vo) throws Exception {
    	Map<String, String> map = new HashMap<>();
        int result = service.complete(vo.getAdoptionBoardNo());
        if (result == 1) {
        	map.put("msg", "good");
        } else {
        	map.put("msg", "bad");
        }
        return map;
    }
    
	// 입양신청 삭제
	@DeleteMapping
	public Map<String, String> delete(@RequestBody AdoptionVo vo) {
		
		Map<String, String> map = new HashMap<String, String>();
		int result = service.delete(vo);
		
		if (result == 1) {
			map.put("msg", "success");
				System.out.println("게시글 삭제 성공 !");
		} else {
			map.put("msg", "fail");
				System.out.println("게시글 삭제 실패 ...");
		}
		
		return map;
	}
	
}
