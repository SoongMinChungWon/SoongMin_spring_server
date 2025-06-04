package com4table.ssupetition.domain.s3.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ser.Serializers;

import com4table.ssupetition.domain.s3.dto.PhotoDto;
import com4table.ssupetition.domain.s3.service.S3ServiceImpl;
import com4table.ssupetition.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/upload")
@Tag(name = "이미지 업로드 API", description = "이미지 업로드 API에 대한 설명입니다.")
@RequiredArgsConstructor
public class S3Controller {

	private final S3ServiceImpl s3Service;


	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(description = "file만 받아서 그냥 이미지 업로드하면 됨 그러면 url String으로 줌")
	public BaseResponse<?> uploadImage(@RequestParam("file") MultipartFile file){
		return BaseResponse.builder()
			.isSuccess(true)
			.code(200)
			.message("이미지가 업로드 되었습니다.")
			.data(s3Service.uploadImage(file))
			.build();
	}
}
