package com4table.ssupetition.domain.news.entitiy;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "news",
	uniqueConstraints = @UniqueConstraint(columnNames = "url"))
@Getter
@Setter
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false, length=512)
	private String url;           // UNIQUE

	@Column(nullable=false, length=300)
	private String title;

	private LocalDate date;       // yyyy-MM-dd

	@Column(length=512)
	private String imageUrl;

	@Lob
	private String content;       // 원문(선택)

	@Column(nullable=false, length=50)
	private String source = "SSU-주요뉴스";

	private Instant createdAt;
	private Instant updatedAt;

	@PrePersist
	void onCreate(){ createdAt = updatedAt = Instant.now(); }

	@PreUpdate
	void onUpdate(){ updatedAt = Instant.now(); }
}