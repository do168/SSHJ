package sshj.sshj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sshj.sshj.dto.FileUploadDto;

/**
 * 파일 업로드 mapper
 * @author krims
 *
 */
@Mapper
public interface S3FileMapper {
	
	public int uploadProfile(@Param("userId") long userId, @Param("profileUrl") String profileUrl);
	
	public int uploadContent(FileUploadDto fileUploadDto);
	
	public List<String> getMeetingFiles(@Param("meetingId") long meetingId);
	
}
