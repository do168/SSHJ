package sshj.sshj.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.SimpleFileDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.UploadService;


/**
 * 파일 업로드 컨트롤러
 * @author krims
 *
 */
@Slf4j
@Api(value="UploadController", description="UploadController")
@RequestMapping("/upload")
@RestController
public class UploadController {

	@Autowired
	private UploadService uploadService;	
	
	@ApiOperation(
		value = "프로필 파일 업로드"
		, notes = "프로필 파일 업로드"
			,authorizations = {@Authorization(value = "JWT")}
	)
	@ApiResponses(value={
		@ApiResponse(code=200, message="")
	})
	@RequestMapping(value = "/profile", method=RequestMethod.POST)
	public ResponseEntity<String> profileUpload(
		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
		@RequestParam("file")MultipartFile multipartFile) throws IOException {
		String imgUrl = uploadService.executeUploadProfile(multipartFile, userHeaderModel.getUserId());	
		
		log.info("profile files uploaded[{}]", imgUrl);		
		
		return new ResponseEntity<String>(imgUrl, HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "동아리 컨텐츠 파일 업로드", notes = "동아리 컨텐츠 파일 업로드"
			,authorizations = {@Authorization (value = "JWT")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "") })
	@RequestMapping(value = "/club", method = RequestMethod.POST)
	public ResponseEntity<List<SimpleFileDto>> contentUpload(
			@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
			MultipartHttpServletRequest multipartHttpServletRequest,
			@ApiParam("meetingId") long meetingId) throws IOException {
		
		if (ObjectUtils.isEmpty(multipartHttpServletRequest))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		List<SimpleFileDto> imgUrls = uploadService.executeUploadClubContents(multipartHttpServletRequest, userHeaderModel.getUserId(), meetingId);
		log.info("club contents uploaded [{}]", imgUrls);

		return new ResponseEntity<List<SimpleFileDto>>(imgUrls, HttpStatus.OK);
	}
}
