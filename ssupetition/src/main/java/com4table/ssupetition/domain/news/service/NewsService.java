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
	private static final long   DELAY_MS = 500L; // 서버 예절상 딜레이
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일");

	/** 주요뉴스 크롤링: 1페이지부터 maxPages까지 */
	public CrawlResult crawlMajorNews(int maxPages) {
		// WebDriver 설정 (USaintCrawler와 동일한 형태 유지)
		ChromeOptions options = new ChromeOptions();
		options.setBinary("/usr/bin/google-chrome");
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
							log.info("[OK] {} {}", item.getDate(), item.getTitle());
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

	/** 목록 페이지에서 상세 링크 뽑기 */
	private List<String> extractLinksOnList(WebDriver driver, int page) {
		String url = (page == 1) ? BASE_URL : BASE_URL + "?paged=" + page;
		log.info("GET {}", url);
		driver.get(url);

		// 앵커 로드 대기
		new WebDriverWait(driver, Duration.ofSeconds(10))
			.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//a[contains(@href,'slug=')]")
			));

		// 상세로 가는 링크가 보통 slug= 파라미터를 포함
		List<WebElement> anchors = driver.findElements(By.xpath("//a[contains(@href, 'slug=')]"));
		List<String> links = anchors.stream()
			.map(a -> a.getAttribute("href"))
			.filter(Objects::nonNull)
			.map(h -> h.split("#")[0])
			.filter(h -> h.contains("slug="))
			.distinct()
			.collect(Collectors.toList());

		log.info("page={} links={}", page, links.size());
		return links;
	}

	/** 상세 페이지 파싱: 제목/날짜/본문/URL */
	private NewsItem parseArticle(WebDriver driver, String url) {
		log.debug("Parse {}", url);
		driver.get(url);

		String title = "";
		try {
			WebElement h = new WebDriverWait(driver, Duration.ofSeconds(8))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h1|//h2)[1]")));
			title = Optional.ofNullable(h.getText()).orElse("").trim();
		} catch (TimeoutException ignored) {}

		// 본문: article p 우선, 없으면 모든 p
		List<WebElement> paras = driver.findElements(By.cssSelector("article p"));
		if (paras.isEmpty()) paras = driver.findElements(By.tagName("p"));
		String content = paras.stream()
			.map(WebElement::getText)
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.collect(Collectors.joining("\n"));

		// 날짜: 페이지 전체 텍스트에서 yyyy년 m월 d일
		String bodyText = driver.findElement(By.tagName("body")).getText();
		Matcher m = DATE_PAT.matcher(bodyText);
		String dateStr = "";
		if (m.find()) {
			int y = Integer.parseInt(m.group(1));
			int mo = Integer.parseInt(m.group(2));
			int d = Integer.parseInt(m.group(3));
			dateStr = String.format("%04d-%02d-%02d", y, mo, d);
		}

		return new NewsItem(title, dateStr, url, content);
	}

	// ====== 결과/아이템 DTO (USaintCrawler의 내부 static 클래스 스타일로 구성) ======

	@Getter
	public static class NewsItem {
		private final String title;
		private final String date;   // yyyy-MM-dd
		private final String url;
		private final String content;

		public NewsItem(String title, String date, String url, String content) {
			this.title = title;
			this.date = date;
			this.url = url;
			this.content = content;
		}
	}

	@Getter
	@Setter
	public static class CrawlResult {
		private final boolean success;
		private final List<NewsItem> items;
		private final String message;

		public CrawlResult(boolean success, List<NewsItem> items, String message) {
			this.success = success;
			this.items = items;
			this.message = message;
		}
	}

}
