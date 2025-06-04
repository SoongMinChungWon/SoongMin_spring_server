package com4table.ssupetition.domain.s3.service;

import org.springframework.web.multipart.MultipartFile;

import com4table.ssupetition.domain.s3.dto.PhotoDto;

public interface S3Service {

	public String uploadImage(MultipartFile file);


}
