package com4table.ssupetition.domain.post.dto.gpt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCompletionRequest {
	private String model;
	private List<Message> messages;
	private Double temperature;
	private Integer max_tokens;

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Message {
		private String role;      // "system" | "user"
		private String content;
	}
}
