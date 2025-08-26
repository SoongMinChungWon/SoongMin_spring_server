# 1) JDK 베이스 이미지
FROM openjdk:17-jdk-slim

# 2) 필요한 패키지 설치 (Chrome 설치용 키, unzip 등)
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    gnupg \
    ca-certificates \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libxss1 \
    libnss3 \
    libx11-6 \
    libgtk-3-0 \
    libgbm-dev \
    libu2f-udev \
    libvulkan1 \
    --no-install-recommends && \
  rm -rf /var/lib/apt/lists/*

# 3) Google Chrome 설치
RUN wget -qO - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" \
      > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*


ARG CHROME_DRIVER_VERSION=139.0.7258.138
RUN wget -O /tmp/chromedriver.zip \
    "https://storage.googleapis.com/chrome-for-testing-public/${CHROME_DRIVER_VERSION}/linux64/chromedriver-linux64.zip" && \
    unzip /tmp/chromedriver.zip -d /tmp && \
    mv /tmp/chromedriver-linux64/chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /tmp/chromedriver.zip /tmp/chromedriver-linux64



# 5) 애플리케이션 JAR 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 6) 컨테이너 기동 시 실행할 커맨드
ENTRYPOINT ["java","-Dwebdriver.chrome.driver=/usr/local/bin/chromedriver","-jar","/app.jar"]
