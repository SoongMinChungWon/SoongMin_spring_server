package com4table.ssupetition.domain.mail.service;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.repository.PostAnswerRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

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

//
//    @Transactional
//    public void saveReply(Long postId, String replyContent) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
//
//        PostAnswer answer = PostAnswer.builder()
//                        .postId(post)
//                        .postAnswerContent(replyContent)
//                        .build();
//        postAnswerRepository.save(answer);
//    }
//
//    @PostConstruct
//    public void startEmailMonitoring() {
//        Thread emailMonitorThread = new Thread(() -> {
//            try {
//                monitorEmail();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        emailMonitorThread.start();
//    }
//
//    public void monitorEmail() throws Exception {
//        Properties properties = new Properties();
//        properties.put("mail.store.protocol", "imaps");
//
//        Session emailSession = Session.getDefaultInstance(properties);
//        Store store = emailSession.getStore("imaps");
//
//        store.connect("imap.naver.com", "ssupetition@naver.com", "XXBS68Y167B8");
//
//        Folder emailFolder = store.getFolder("INBOX");
//        emailFolder.open(Folder.READ_ONLY);
//
//        while (true) {
//            jakarta.mail.Message[] messages = emailFolder.getMessages();
//            for (Message message : messages) {
//                if (message.getSubject().startsWith("RE:")) {  // Assuming the subject of the reply starts with "RE:"
//                    Long postId = extractPostIdFromSubject(message.getSubject());
//                    String content = message.getContent().toString();
//                    saveReply(postId, content);
//                }
//            }
//            Thread.sleep(60000); // Check for new emails every 60 seconds
//        }
//    }
//
//    private Long extractPostIdFromSubject(String subject) {
//        // Assuming the subject contains the post ID in a specific format
//        // Example: "RE: [PostId: 123] Original Subject"
//        String[] parts = subject.split("\\[PostId: ");
//        if (parts.length > 1) {
//            String postIdStr = parts[1].split("\\]")[0];
//            return Long.parseLong(postIdStr);
//        }
//        throw new IllegalArgumentException("Invalid subject format");
//    }
}
