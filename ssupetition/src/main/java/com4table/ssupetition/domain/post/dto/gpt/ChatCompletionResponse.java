package com4table.ssupetition.domain.post.dto.gpt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionResponse {


		private List<Choice> choices;

		@Getter @Setter @NoArgsConstructor @AllArgsConstructor
		public static class Choice {
			private int index;
			private Message message;

			@Getter @Setter @NoArgsConstructor @AllArgsConstructor
			public static class Message {
				private String role;
				private String content;
			}
		}
}
