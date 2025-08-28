package com4table.ssupetition.domain.news.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com4table.ssupetition.domain.news.entitiy.News;
import com4table.ssupetition.domain.news.repository.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsStoreService {
	private final NewsRepository newsRepository;

	@Transactional
	public News upsertByUrl(NewsService.NewsItem item) {
		News news = newsRepository.findByUrl(item.getUrl()).orElseGet(News::new);
		news.setUrl(item.getUrl());
		news.setTitle(item.getTitle());
		news.setImageUrl(item.getImageUrl());
		news.setContent(item.getContent());

		if (item.getDate() != null && !item.getDate().isBlank()) {
			try {
				news.setDate(LocalDate.parse(item.getDate()));
			} catch (Exception ignored) {}
		}
		return newsRepository.save(news);
	}
}