package com4table.ssupetition.domain.login.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class USaintCrawler {

    private static final String LOGIN_URL = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
    private static final String USER_INFO_URL = "https://saint.ssu.ac.kr/irj/portal";
    private static final String USER_INFO_URL_JSP = "https://saint.ssu.ac.kr/webSSUMain/main_student.jsp";

    public LoginResult loginAndGetInfo(String id, String password) {
        // WebDriver 설정
        String driverPath = System.getProperty("user.dir") + "/drivers/chromedriver-linux64/chromedriver";
        //String driverPath = System.getProperty("user.dir") + "/drivers/chromedriver-mac-arm64/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드로 실행
        WebDriver driver = new ChromeDriver(options);

        try {
            // 로그인 페이지 요청
            driver.get(LOGIN_URL);

            // 로그인 요청
            WebElement userIdField = driver.findElement(By.name("userid"));
            WebElement passwordField = driver.findElement(By.name("pwd"));
            WebElement submitButton = driver.findElement(By.cssSelector("a.btn_login")); // 로그인 버튼 클래스 선택

            userIdField.sendKeys(id);
            passwordField.sendKeys(password);
            submitButton.click();

            // 로그인 완료 대기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3)); // Selenium 4.x에 맞게 수정
            wait.until(ExpectedConditions.urlToBe(USER_INFO_URL));

            // 로그인 성공 확인
            boolean loginSuccess = isLoginSuccessful(driver);

            log.info("Check login success: {}", loginSuccess);

            if (loginSuccess) {
                // 로그인 후 직접 원하는 페이지로 이동
                driver.get(USER_INFO_URL_JSP);


                // 사용자 정보 추출
                String studentId = getTextByXpath(driver, "/html/body/div/div[1]/div[1]/div[1]/div[2]/ul/li[1]/dl/dd/a/strong");
                String department = getTextByXpath(driver, "/html/body/div/div[1]/div[1]/div[1]/div[2]/ul/li[2]/dl/dd/a/strong");
                String userName = getUserName(driver);

                log.info("학번: {}, 소속: {}, 이름: {}", studentId, department, userName);

                // ID를 파일에서 추출하는 부분은 웹 페이지에서 직접 얻어오는 것으로 대체
                String extractedId = studentId; // 여기서는 추출된 학번을 직접 사용

                if (id.equals(extractedId)) {
                    return new LoginResult(true, new UserInfo(studentId, userName, department));
                } else {
                    log.info("ID does not match: loginId = {}, extractedId = {}", id, extractedId);
                    return new LoginResult(false, null);
                }
            } else {
                return new LoginResult(false, null);
            }
        } finally {
            driver.quit(); // 브라우저 종료
        }
    }

    public Boolean userCheck(String id, String password) {
        // WebDriver 설정
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\gwanr\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"); // ChromeDriver 경로 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드로 실행
        WebDriver driver = new ChromeDriver(options);

        try {
            // 로그인 페이지 요청
            driver.get(LOGIN_URL);

            // 로그인 요청
            WebElement userIdField = driver.findElement(By.name("userid"));
            WebElement passwordField = driver.findElement(By.name("pwd"));
            WebElement submitButton = driver.findElement(By.cssSelector("a.btn_login")); // 로그인 버튼 클래스 선택

            userIdField.sendKeys(id);
            passwordField.sendKeys(password);
            submitButton.click();

            // 로그인 완료 대기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3)); // Selenium 4.x에 맞게 수정
            wait.until(ExpectedConditions.urlToBe(USER_INFO_URL));

            // 로그인 성공 확인
            return isLoginSuccessful(driver);
        } finally {
            driver.quit(); // 브라우저 종료
        }
    }

    private String getTextByXpath(WebDriver driver, String xpath) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            return element != null ? element.getText() : "";
        } catch (Exception e) {
            log.error("Error finding text by XPath: ", e);
            return "";
        }
    }

    private boolean isLoginSuccessful(WebDriver driver) {
        try {
            WebElement welcomeMessage = driver.findElement(By.xpath("//span[contains(text(), '환영합니다')]"));
            return welcomeMessage != null;
        } catch (Exception e) {
            log.error("Error checking login success: ", e);
            return false;
        }
    }

    private String getUserName(WebDriver driver) {
        try {
            WebElement welcomeMessage = driver.findElement(By.xpath("/html/body/div/div[1]/div[1]/div[1]/div[1]/p[1]/span"));
            String text = welcomeMessage != null ? welcomeMessage.getText() : "";
            // '님 환영합니다.' 앞의 부분 추출
            if (text.contains("님 환영합니다.")) {
                return text.split("님 환영합니다.")[0].trim();
            }
            return "";
        } catch (Exception e) {
            log.error("Error getting user name: ", e);
            return "";
        }
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
