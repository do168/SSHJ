package sshj.sshj.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sshj.sshj.dto.enums.FileDirEnum;

/**
 * aws S3 컴포넌트
 * @author krims
 *
 */
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:aws.yml")
@Component
public class S3Utils {
	
	private final AmazonS3Client amazonS3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	/**
	 * 
	 * MultipartFile -> File
	 * @param multipartFile
	 * @param dirName
	 * @return
	 * @throws IOException
	 */
	public String upload(MultipartFile multipartFile, FileDirEnum dirName, String newFilename) throws IOException {
		
		File uploadFile = convert(multipartFile);
		String fileName = dirName.name() + "/" + newFilename;
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	/**
	 * S3로 업로드
	 * @param uploadFile
	 * @param fileName
	 * @return
	 */
	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(
				new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	/**
	 * 서버내 파일 제거
	 * @param targetFile
	 */
	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("서버내 파일 삭제 완료.");
		} else {
			log.info("서버내 파일 삭제 실패.");
		}
	}

	/**
	 * MultipartFile -> File 변환
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private File convert(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());

		convertFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;
	}
}
