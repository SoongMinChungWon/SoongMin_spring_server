package com4table.ssupetition.domain.news.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com4table.ssupetition.domain.news.service.NewsService;
import com4table.ssupetition.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "뉴스 크롤링 API")
public class NewsController {

	private final NewsService newsService;


	@GetMapping("")
	@Operation(summary = "뉴스 크롤링해서 정보 가져오는 API입니다.")
	public BaseResponse<NewsService.CrawlResult> getCrawlNews(){
		return BaseResponse.<NewsService.CrawlResult>builder()
			.isSuccess(true)
			.code(200)
			.data(newsService.crawlMajorNews(10))
			.message("뉴스 크롤링에 성공하였습니다.")
			.build();
	}

}
