package com4table.ssupetition.domain.post.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PetitionDtos
{

	@Getter
	@Setter
	public static class GenerateRequest {
		private String category;   // 예: "시설", "수업", "행정"
		private String titleDraft; // 사용자 초안 제목
		private String bodyDraft;  // 사용자 초안 본문
	}

	@Getter @AllArgsConstructor
	public static class GenerateResponse {
		private final String title;
		private final String body;
	}
}
