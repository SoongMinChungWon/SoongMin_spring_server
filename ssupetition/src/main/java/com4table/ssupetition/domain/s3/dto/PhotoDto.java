package com4table.ssupetition.domain.s3.dto;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

public record PhotoDto(
					   MultipartFile multipartFile) {
}
