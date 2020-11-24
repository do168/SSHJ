package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sshj.sshj.mapper.S3FileMapper;

public class ArchiveServiceImpl implements ArchiveService {

	@Autowired
	private S3FileMapper s3FileMapper;
	
	@Override
	public List<String> getArchiveList(long userId) {
		
		return s3FileMapper.selectContentsUrl(userId);
	}
	
	
}
