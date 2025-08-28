package com4table.ssupetition.domain.news.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com4table.ssupetition.domain.news.dto.NewsDto;
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



	// public BaseResponse<NewsService.CrawlResult> getCrawlNews(){
	// 	return BaseResponse.<NewsService.CrawlResult>builder()
	// 		.isSuccess(true)
	// 		.code(200)
	// 		.data(newsService.crawlMajorNews(2))
	// 		.message("뉴스 크롤링에 성공하였습니다.")
	// 		.build();
	// }
	@GetMapping("")
	@Operation(summary = "뉴스 크롤링해서 정보 가져오는 API입니다.")
	public BaseResponse<Page<NewsDto>> list(@PageableDefault(size=12, sort="date", direction = Sort.Direction.DESC) Pageable pageable){;
			return BaseResponse.<Page<NewsDto>>builder()
				.isSuccess(true)
				.code(200)
				.data(newsService.getNews(pageable))
				.message("뉴스 정보를 가져오는데 성공하였습니다.")
				.build();
	}

	@PostMapping("/crawl")
	public Map<String,Object> crawlNow(){
		var r = newsService.crawlMajorNews(3);
		return Map.of("success", r.isSuccess(), "count", r.getItems().size(), "message", r.getMessage());
	}

}
