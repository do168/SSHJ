package sshj.sshj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import sshj.sshj.dto.FileUploadDto;
import sshj.sshj.dto.SimpleFileDto;

/**
 * 파일 업로드 mapper
 * @author krims
 *
 */
@Mapper
@Repository
public interface S3FileMapper {
	
	int uploadProfile(@Param("userId") long userId, @Param("profileUrl") String profileUrl, @Param("bucket type") String bucket);
	
	int uploadContent(FileUploadDto fileUploadDto);
	
	List<SimpleFileDto> getMeetingFiles(@Param("meetingId") long meetingId);

	String selectProfileImage(@Param("userId") long userId, @Param("bucket type") String bucket);

	List<SimpleFileDto> selectContentsUrl(@Param("clubId") long clubId);

	// TODO delete 기능이 필요할까?
	int deleteContent(FileUploadDto fileUploadDto);

	int createRelationFileMeeting(@Param("fileId") long fileId, @Param("meetingId") long meetingId, int index);


}
