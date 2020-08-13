package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;

import sshj.sshj.dto.FileUploadDto;

/**
 * 파일 업로드 mapper
 * @author krims
 *
 */
@Mapper
public interface UploadMapper {
	
	public int uploadProfile(FileUploadDto fileUploadDto);
}
