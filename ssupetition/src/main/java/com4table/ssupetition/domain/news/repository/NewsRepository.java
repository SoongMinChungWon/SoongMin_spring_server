package com4table.ssupetition.domain.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com4table.ssupetition.domain.news.entitiy.News;

public interface NewsRepository extends JpaRepository<News,Long> {
	Optional<News> findByUrl(String url);
}
