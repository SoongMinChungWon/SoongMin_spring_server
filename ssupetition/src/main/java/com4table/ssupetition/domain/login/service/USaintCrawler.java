package com4table.ssupetition.domain.login.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@Transactional
public class USaintCrawler {

    private static final String LOGIN_URL = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
    private static final String USER_INFO_URL = "https://saint.ssu.ac.kr/irj/portal";

    public LoginResult loginAndGetInfo(String id, String password) throws IOException {
        // 로그인 페이지에 접속하여 필요한 쿠키를 수집합니다.
        Connection.Response loginPageResponse = Jsoup.connect(LOGIN_URL)
                .method(Connection.Method.GET)
                .execute();

        // 로그인 요청을 보냅니다.
        Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
                .cookies(loginPageResponse.cookies())
                .data("userid", id, "pwd", password)
                .method(Connection.Method.POST)
                .execute();

        log.info("{}", loginResponse.cookies());

        boolean loginSuccess = isLoginSuccessful(loginResponse);

        log.info("check login success : {}", loginSuccess);

        if (loginSuccess) {
            // 사용자 정보 페이지에 접근하여 데이터를 가져옵니다.
            Document userInfoPage = Jsoup.connect(USER_INFO_URL)
                    .cookies(loginResponse.cookies())
                    .get();

            String userLoginId = getTextByLabel(userInfoPage, "학번");
            String userName = getUserName(userInfoPage);
            String userMajor = getTextByLabel(userInfoPage, "소속");
            log.info("학번:{}, 이름:{}, 소속:{}", userLoginId, userName, userMajor);

            // HTML 파일에서 학번 추출
            String extractedId = extractIdFromHtml("/mnt/data/숭실대학교.html");
            log.info("Extracted ID from HTML: {}", extractedId);

            // 입력한 id와 추출된 학번 비교
            if (id.equals(extractedId)) {
                return new LoginResult(true, new UserInfo(userLoginId, userName, userMajor));
            } else {
                log.info("ID does not match: loginId={}, extractedId={}", id, extractedId);
                return new LoginResult(false, null);
            }
        } else {
            return new LoginResult(false, null);
        }
    }

    private String getTextByLabel(Document document, String label) {
        Element element = document.select("dt:contains(" + label + ") + dd strong").first();
        return element != null ? element.text() : "";
    }

    private boolean isLoginSuccessful(Connection.Response loginResponse) throws IOException {
        Document document = Jsoup.connect(USER_INFO_URL)
                .cookies(loginResponse.cookies())
                .get();

        boolean loggedIn = document.select("p:contains(로그인 페이지)").isEmpty();
        return loggedIn;
    }

    private String getUserName(Document document) throws IOException {
        Element iframeElement = document.select("div#contentAreaDiv iframe#contentAreaFrame.contentIframe").first();
        if (iframeElement != null) {
            String iframeSrc = iframeElement.attr("src");
            Document iframeDocument = Jsoup.connect(iframeSrc).get();
            Element nameElement = iframeDocument.select("div.main_bg > div.main_wrap > div.main_left > div.main_box09 > div.box_top > p.main_title > span").first();
            return nameElement != null ? nameElement.text() : "";
        }
        return "";
    }

    private String extractIdFromHtml(String filePath) throws IOException {
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        Element element = document.selectFirst("dd strong:contains(20201870)"); // 학번을 나타내는 선택자
        return element != null ? element.text() : "";
    }

    @Getter
    public static class UserInfo {
        private final String loginId;
        private final String userName;
        private final String userMajor;

        public UserInfo(String loginId, String userName, String userMajor) {
            this.loginId = loginId;
            this.userName = userName;
            this.userMajor = userMajor;
        }
    }

    @Getter
    @Setter
    public static class LoginResult {
        private final boolean success;
        private final UserInfo userInfo;

        public LoginResult(boolean success, UserInfo userInfo) {
            this.success = success;
            this.userInfo = userInfo;
        }
    }
}
