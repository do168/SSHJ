package sshj.sshj.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 파일 업로드 서비스
 * @author krims
 *
 */
public interface UploadService {

	/**
	 * 프로필 파일 업로드
//	 * @param multipartHttpServletRequest
	 * @param userId
	 * @return
	 */
	public String executeUploadProfile(MultipartFile multipartFile, long userId);

	/**
	 * 동아리 컨텐츠 파일 업로드
	 * @param multipartHttpServletRequest
	 * @param userId
	 * @return
	 */
	public List<String> executeUploadClubContents(MultipartHttpServletRequest multipartHttpServletRequest, long userId, long meetingId);




	public String executeDownloadProfile(long userId, String bucket);
}

