package sshj.sshj.dto;

import lombok.Data;

/**
 * 업로드 파일 정보
 * @author krims
 *
 */
@Data
public class FileUploadDto {
	
	/**
	 * 파일 번호
	 */
	private long fileId;

	/**
	 * 등록 유저 번호
	 */
	private long userId;

	/**
	 * 원본 파일명
	 */
	private String originFileName;
	
	/**
	 * 파일 저장 경로
	 */
	private String fileUrl;
	
	/**
	 * 저장 파일명
	 */
	private String fileName;
	
	/**
	 * 파일 타입
	 */
	private String mimeType;
	
	/**
	 * 파일 크기
	 */
	private long size;
	
	/**
	 * 모임 번호
	 */
//	private long meetingId;
	
	/**
	 * 업로드 시간
	 */
	private String uploadDatetime;

	/**
	 * 배열 순서
	 */
	private int index;
}
