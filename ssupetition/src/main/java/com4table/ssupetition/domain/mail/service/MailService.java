package com4table.ssupetition.domain.mail.service;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.post.repository.PostAnswerRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostAnswerRepository postAnswerRepository;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ssupetition@naver.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendPostNotification(Long postId, String to, String subject, String text) {
        String subjectWithId = "[PostId: " + postId + "] " + subject;
        sendSimpleMessage(to, subjectWithId, text);
    }

    @Transactional
    public void saveReplyAndSetType(Long postId, String replyContent) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        PostAnswer answer = PostAnswer.builder()
                .postId(post)
                .postAnswerContent(replyContent)
                .build();

        post.setPostType(Type.state4);
        postRepository.save(post);
        postAnswerRepository.save(answer);
    }

    @Scheduled(fixedDelay = 60000) // 60초마다 실행
    public void monitorEmail() {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.host", "imap.naver.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.ssl.enable", "true");
            properties.put("mail.debug", "true");

            Session emailSession = Session.getDefaultInstance(properties);
            emailSession.setDebug(true);

            Store store = emailSession.getStore("imaps");
            store.connect("imap.naver.com", "ssupetition@naver.com", "XXBS68Y167B8");

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE); // Read-write 모드로 폴더 열기

            Message[] messages = emailFolder.getMessages();
            for (Message message : messages) {
                if(message.getFlags().contains(Flags.Flag.SEEN)) continue;


                if (message.getSubject().startsWith("Re: [Code:")) {
                    Long postId = extractPostIdFromSubject(message.getSubject());
                    String content = extractContent(message);
                    log.info("message title:{}",message.getSubject());
                    log.info("message content:{}//////////////끝", content);

                    saveReplyAndSetType(postId, content);

                    // 메일을 읽음 처리
                    message.setFlag(Flags.Flag.SEEN, true);
                }
            }

            emailFolder.close(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String extractContent(Message message) throws IOException, MessagingException {
        if (message.isMimeType("text/plain")) {
            log.info("1");
            return message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            log.info("2");
            return message.getContent().toString(); // HTML을 텍스트로 변환할 필요가 있을 수 있음
        } else if (message.getContent() instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            log.info("3");
            return getTextFromMimeMultipart(multipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart multipart) throws MessagingException, IOException {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.getContentType().contains("text/plain")) {
                //if(bodyPart.getContent().toString().equals("On")) break;
                text.append(bodyPart.getContent().toString());
            } else if (bodyPart.getContentType().contains("text/html")) {
                // HTML을 텍스트로 변환할 필요가 있을 수 있음
                //text.append(bodyPart.getContent().toString());
            }
        }
        return text.toString();
    }

    private Long extractPostIdFromSubject(String subject) {
        String[] parts = subject.split("\\[Code: ");
        if (parts.length > 1) {
            String postIdStr = parts[1].split("\\]")[0];
            return Long.parseLong(postIdStr);
        }
        throw new IllegalArgumentException("Invalid subject format");
    }
}