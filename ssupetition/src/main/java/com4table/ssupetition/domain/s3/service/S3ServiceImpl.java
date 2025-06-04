package com4table.ssupetition.domain.s3.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com4table.ssupetition.domain.s3.dto.PhotoDto;
import com4table.ssupetition.global.exception.BaseException;
import com4table.ssupetition.global.exception.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service{
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	public String uploadImage(MultipartFile file) {

		S3Client s3 = S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(
				AwsBasicCredentials.create(accessKey, secretKey)
			))
			.build();

		try {
			String fileName = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.contentType(file.getContentType())
				.build();

			PutObjectResponse response = s3.putObject(
				putObjectRequest,
				software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
			);

			log.info("S3 Upload Response: {}", response);
			String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);

			return fileUrl;

		} catch (S3Exception | IOException e) {
			log.error("S3 upload error", e);
			throw new BaseException(BaseResponseStatus.UPLOAD_FAIL);
		} finally {
			s3.close();
		}
	}

}
