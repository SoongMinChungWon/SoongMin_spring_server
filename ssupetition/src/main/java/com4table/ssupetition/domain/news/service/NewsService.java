package com4table.ssupetition.domain.news.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

	private static final String BASE_URL = "https://scatch.ssu.ac.kr/뉴스센터/주요뉴스/";
	private static final long   DELAY_MS = 500L;
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일");

	private final NewsStoreService newsStoreService; // ⬅️ 저장 서비스 주입

	/** 주요뉴스 크롤링: 1페이지부터 maxPages까지 */
	public CrawlResult crawlMajorNews(int maxPages) {
		ChromeOptions options = new ChromeOptions();
		options.setBinary("/usr/bin/google-chrome");        // Docker 리눅스 이미지 기준 경로 유지
		options.addArguments("--headless=new");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		WebDriver driver = new ChromeDriver(options);

		List<NewsItem> results = new ArrayList<>();
		Set<String> seen = new HashSet<>();

		try {
			int page = 1;
			while (true) {
				if (maxPages > 0 && page > maxPages) break;

				List<String> links = extractLinksOnList(driver, page);
				if (links.isEmpty()) {
					log.info("No more links at page={}", page);
					break;
				}
				for (String link : links) {
					if (!seen.add(link)) continue;
					try {
						NewsItem item = parseArticle(driver, link);
						if ((item.getTitle() != null && !item.getTitle().isEmpty())
							|| (item.getDate() != null && !item.getDate().isEmpty())) {
							results.add(item);
							// ⬇️ 여기서 바로 DB 업서트 (URL unique)
							newsStoreService.upsertByUrl(item);
							log.info("[OK] {} {} (img={})", item.getDate(), item.getTitle(), item.getImageUrl());
						} else {
							log.info("[SKIP] {} (empty)", link);
						}
						Thread.sleep(DELAY_MS);
					} catch (Exception e) {
						log.warn("[ERR] {} :: {}", link, e.toString());
					}
				}
				page++;
			}
			return new CrawlResult(true, results, "count=" + results.size());
		} catch (Exception e) {
			log.error("crawlMajorNews error: ", e);
			return new CrawlResult(false, results, "error=" + e.getMessage());
		} finally {
			driver.quit();
		}
	}

	/** 목록 페이지에서 상세 링크 뽑기 (현행 유지) */
	private List<String> extractLinksOnList(WebDriver driver, int page){

		String url = (page == 1) ? BASE_URL : BASE_URL + "?paged=" + page; log.info("GET {}", url); driver.get(url);
		new WebDriverWait(driver, Duration.ofSeconds(10)) .until(ExpectedConditions.presenceOfElementLocated( By.xpath("//a[contains(@href,'slug=')]") ));
		 List<WebElement> anchors = driver.findElements(By.xpath("//a[contains(@href, 'slug=')]"));
		 List<String> links = anchors.stream() .map(a -> a.getAttribute("href")) .filter(Objects::nonNull) .map(h -> h.split("#")[0]) .filter(h -> h.contains("slug=")) .distinct() .collect(Collectors.toList());
		 log.info("page={} links={}", page, links.size());
		 return links;

	}

	/** 상세 페이지 파싱: 제목/날짜/본문/URL + 대표 이미지 */
	private NewsItem parseArticle(WebDriver driver, String url) {
		log.debug("Parse {}", url);
		driver.get(url);

		String title = "";
		try {
			WebElement h = new WebDriverWait(driver, Duration.ofSeconds(8))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1|//h2)[1]")));
			title = Optional.ofNullable(h.getText()).orElse("").trim();
		} catch (TimeoutException ignored) {}

		// 본문: article p 우선, 없으면 모든 p (현행 유지)
		List<WebElement> paras = driver.findElements(By.cssSelector("article p"));
		if (paras.isEmpty()) paras = driver.findElements(By.tagName("p"));
		String content = paras.stream()
			.map(WebElement::getText)
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.collect(Collectors.joining("\n"));

		// 날짜: 페이지 전체 텍스트에서 yyyy년 m월 d일 (현행 유지)
		String bodyText = driver.findElement(By.tagName("body")).getText();
		Matcher m = DATE_PAT.matcher(bodyText);
		String dateStr = "";
		if (m.find()) {
			int y = Integer.parseInt(m.group(1));
			int mo = Integer.parseInt(m.group(2));
			int d = Integer.parseInt(m.group(3));
			dateStr = String.format("%04d-%02d-%02d", y, mo, d);
		}

		// ⬇️ 대표 이미지 추출(og:image → article img → img)
		String imageUrl = null;
		try {
			List<WebElement> ogImgs = driver.findElements(By.cssSelector("meta[property='og:image']"));
			if (!ogImgs.isEmpty()) {
				String c = ogImgs.get(0).getAttribute("content");
				if (c != null && !c.isBlank()) imageUrl = c.trim();
			}
			if (imageUrl == null) {
				List<WebElement> imgs = driver.findElements(By.cssSelector("article img"));
				if (imgs.isEmpty()) imgs = driver.findElements(By.tagName("img"));
				if (!imgs.isEmpty()) {
					String src = imgs.get(0).getAttribute("src");
					if (src != null && !src.isBlank()) imageUrl = src.trim();
				}
			}
		} catch (Exception ignored) {}

		return new NewsItem(title, dateStr, url, content, imageUrl);
	}

	// ====== DTO ======
	@Getter
	public static class NewsItem {
		private final String title;
		private final String date;   // yyyy-MM-dd
		private final String url;
		private final String content;   // (유지)
		private final String imageUrl;  // (신규)

		public NewsItem(String title, String date, String url, String content, String imageUrl) {
			this.title = title;
			this.date = date;
			this.url = url;
			this.content = content;
			this.imageUrl = imageUrl;
		}
	}

	@Getter @Setter
	public static class CrawlResult {
		private final boolean success;
		private final List<NewsItem> items;
		private final String message;
		public CrawlResult(boolean success, List<NewsItem> items, String message) {
			this.success = success; this.items = items; this.message = message;
		}
	}
}
