package sshj.sshj.service;

import java.util.List;

import org.springframework.stereotype.Service;
import sshj.sshj.dto.SimpleFileDto;

/**
 * 아카이브 (파일) 서비스
 * @author krims
 *
 */
@Service
public interface ArchiveService {

	/**
	 * 유저별 아카이브 파일 리스트 조회
	 */
	public List<SimpleFileDto> getArchiveList(long userId);
}
