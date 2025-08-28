package com4table.ssupetition.domain.news.dto;

import java.time.LocalDate;

import com4table.ssupetition.domain.news.entitiy.News;

public record NewsDto(
	Long id,
	String title,
	LocalDate date,
	String url,
	String imageUrl
) {
	public static NewsDto from(News n){
		return new NewsDto(n.getId(), n.getTitle(), n.getDate(), n.getUrl(), n.getImageUrl());
	}
}