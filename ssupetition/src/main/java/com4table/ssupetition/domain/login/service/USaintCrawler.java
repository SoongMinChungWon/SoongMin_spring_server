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

import java.io.IOException;

@Slf4j
@Service
@Transactional
public class USaintCrawler {

    private static final String LOGIN_URL = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
    private static final String USER_INFO_URL = "https://saint.ssu.ac.kr/irj/portal";

    /**
     * 유세인트에 로그인하고 정보를 가져옵니다.
     * @param id 사용자 ID
     * @param password 사용자 비밀번호
     * @return 로그인 및 정보 추출 결과
     * @throws IOException
     */
    public LoginResult loginAndGetInfo(String id, String password) throws IOException {
        // 로그인 폼 데이터 설정
        Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
                .data("userid", id, "pwd", password)
                .method(Connection.Method.POST)
                .execute();

        // 로그인 성공 여부 확인
        boolean loginSuccess = isLoginSuccessful(loginResponse);

        log.info("check login success : {}", loginSuccess);

        if (loginSuccess) {
            // 로그인 후 유저 정보 페이지 접근
            Document userInfoPage = Jsoup.connect(USER_INFO_URL)
                    .cookies(loginResponse.cookies())
                    .get();

            String userLoginId = getTextByLabel(userInfoPage, "학번");
            String userName = getUserName(userInfoPage);
            String userMajor = getTextByLabel(userInfoPage, "소속");
            log.info("학번:{}, 이름:{}, 소속:{}",userLoginId,userName,userMajor);

            return new LoginResult(true, new UserInfo(userLoginId, userName, userMajor));
        } else {
            return new LoginResult(false, null);
        }
    }
    private String getTextByLabel(Document document, String label) {
        Element element = document.select("dt:contains(" + label + ") + dd strong").first();
        return element != null ? element.text() : "";
    }

    /**
     * 로그인 성공 여부를 확인합니다.
     * @param loginResponse 로그인 후 받은 Response 객체
     * @return 로그인 성공 여부
     * @throws IOException
     */
    private boolean isLoginSuccessful(Connection.Response loginResponse) throws IOException {
        // 로그인 후 접근 가능한 페이지에서 로그인 성공 여부를 확인하는 로직
        Document document = Jsoup.connect(USER_INFO_URL)
                .cookies(loginResponse.cookies())
                .get();

        // 예시로 로그인 페이지에 특정 요소가 없을 경우 실패로 간주
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
