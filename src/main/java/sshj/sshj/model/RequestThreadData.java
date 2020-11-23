package sshj.sshj.model;

import java.util.Map;

import lombok.Data;
import sshj.sshj.dto.UserHeaderModel;

/**
 * 요청 데이터
 * @author bumworld
 *
 */
@Data
public class RequestThreadData {

	/**
	 * 사용자 인증 정보
	 */
	private UserHeaderModel userAuthModel;

	/**
	 * 헤더 맵
	 */
	private Map<String, String> headerMap;

	/**
	 * 파라미터 맵
	 */
	private Map<String, Object> paramMap;

	/**
	 * user agent
	 */
	private Map<String, String> userAgentMap;

	/**
	 * user agent
	 */
	private String userAgent;

	/**
	 * RequestURI
	 */
	private String requestURI;

	/**
	 * body map
	 */
	private Map bodyMap;

	/**
	 * 시작 아이디
	 */
	private long startThreadId;

	/**
	 * 종료 아이디
	 */
	private long endThreadId;

	/**
	 * 요청 주소
	 */
	private String requestIpAddress;
}