package com4table.ssupetition.domain.news.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com4table.ssupetition.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsJob {

	private final NewsService newsService;

	// 1) 30분마다 정기 실행
	@Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
	public void crawlMajorNewsJob(){
		runCrawl("scheduled");
	}

	// 2) 앱 시작 직후 1회 실행
	@org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void crawlOnceAfterStartup() {
		// 비동기로 돌리고 싶으면 @Async 붙이거나 별도 executor 사용
		runCrawl("startup");
	}

	private void runCrawl(String reason){
		try {
			var result = newsService.crawlMajorNews(3); // 필요한 페이지만
			if (result.isSuccess()) {
				log.info("[NewsJob:{}] updated items = {}", reason, result.getItems().size());
			} else {
				log.warn("[NewsJob:{}] failed: {}", reason, result.getMessage());
			}
		} catch (Exception e){
			log.error("[NewsJob:{}] unexpected error", reason, e);
		}
	}
}