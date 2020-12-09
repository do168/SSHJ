package sshj.sshj.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.SimpleFileDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.ArchiveService;

/**
 * 아카이브(개인 파일 저장소) 컨트롤러
 * @author krims
 *
 */
@Slf4j
@Api(value="ArchiveController")
@RequestMapping("/archive")
@RestController
public class ArchiveController {

	@Autowired
	private ArchiveService archiveService;	
	
	@ApiOperation(
			value = "아카이브 파일 업로드"
			, notes = "아카이브 파일 업로드"
			, authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "") })
	@RequestMapping(value = "/get/list", method = RequestMethod.GET)
	public ResponseEntity<List<SimpleFileDto>> getArchiveList(
			@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws IOException {
		
		List<SimpleFileDto> fileDtoList = archiveService.getArchiveList(userHeaderModel.getUserId());

		return new ResponseEntity<List<SimpleFileDto>>(fileDtoList, HttpStatus.OK);
	}
	
	
	
	
	
// TODO: 아카이브 내부에서도 upload 가능하도록 구현 할 예정 우선순위가 낮아 추후 개발 (재훈)
//	@ApiOperation(
//		value = "아카이브 파일 업로드"
//		, notes = "아카이브 파일 업로드"
//			,authorizations = {@Authorization(value = "JWT")}
//	)
//	@ApiResponses(value={
//		@ApiResponse(code=200, message="")
//	})
//	@RequestMapping(value = "/upload", method=RequestMethod.POST)
//	public ResponseEntity<String> profileUpload(
//		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
//		@RequestParam("file")MultipartFile multipartFile) throws IOException {
//		String imgUrl = uploadService.executeUploadProfile(multipartFile, userHeaderModel.getUserId());	
//		// TODO: 1=admin userInfoModel 생성시 교체
//		log.info("profile files uploaded[{}]", imgUrl);		
//		
//		return new ResponseEntity<String>(imgUrl, HttpStatus.OK);
//	}
}
