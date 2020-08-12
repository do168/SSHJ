package sshj.sshj.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import sshj.sshj.component.S3Utils;
import sshj.sshj.dto.FileUploadDto;
import sshj.sshj.dto.enums.FileDirEnum;
import sshj.sshj.mapper.UploadMapper;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService{
	
	@Autowired
	private UploadMapper uploadMapper;
	
	@Autowired
	private S3Utils s3Utils;
	
	/**
	 * 프로필 파일 업로드
	 */
	@Override
	public List<String> executeUploadProfile(MultipartHttpServletRequest multipartHttpServletRequest, long userId) {
		return uploadFile(multipartHttpServletRequest, userId, FileDirEnum.profile);
	}
	
	/**
	 * 동아리 컨텐츠 업로드
	 */
	@Override
	public List<String> executeUploadClubContents(MultipartHttpServletRequest multipartHttpServletRequest, long userId){
		return uploadFile(multipartHttpServletRequest, userId, FileDirEnum.club);
	}
	
	/**
	 * 파일 업로드 로직
	 */
	private List<String> uploadFile(MultipartHttpServletRequest multipartHttpServletRequest, long userId, FileDirEnum fileDirEnum){

		Iterator<String> fileList = multipartHttpServletRequest.getFileNames();
		List<String> imgUrls = new ArrayList<String>();
		
		while(fileList.hasNext()) {
			String newFilename = Long.toString(System.nanoTime());
			String imgUrl = "";
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(fileList.next());
			for(MultipartFile file : list) {
				try {
					String mimeType = new Tika().detect(file.getInputStream());

					FileUploadDto fileUploadDto = new FileUploadDto();
					fileUploadDto.setUserId(userId);
					fileUploadDto.setOriginFileName(file.getOriginalFilename());
					fileUploadDto.setFileBaseUrl("/" + fileDirEnum.name());
					fileUploadDto.setFileName(newFilename);
					fileUploadDto.setMimeType(mimeType);
					fileUploadDto.setSize(file.getSize());
					
					int cnt = uploadMapper.uploadProfile(fileUploadDto);

					if (cnt != 1) {
						log.error("Save upload info failed!!");
						throw new RuntimeException();
					}
					
					imgUrl = s3Utils.upload(file, fileDirEnum, newFilename);

					imgUrls.add(imgUrl);

				} catch (IOException e) {
					log.error("", e);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return imgUrls;
	}
}
