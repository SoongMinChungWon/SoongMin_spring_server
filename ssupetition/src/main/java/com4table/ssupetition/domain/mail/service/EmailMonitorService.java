package com4table.ssupetition.domain.mail.service;


import com4table.ssupetition.domain.mail.websocket.WebSocketHandler;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.repository.PostAnswerRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.post.service.PostAnswerService;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.imap.IMAPFolder;
import org.eclipse.angus.mail.imap.IMAPStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailMonitorService {
    private static final String HOST = "imap.naver.com";
    private static final String USERNAME = "your-email@naver.com";
    private static final String PASSWORD = "your-email-password";

    private final PostAnswerService postAnswerService;


    @Scheduled(fixedRate = 600000)
    public void checkForNewEmails() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", HOST);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            Session emailSession = Session.getDefaultInstance(properties);
            IMAPStore emailStore = (IMAPStore) emailSession.getStore("imap");
            emailStore.connect(USERNAME, PASSWORD);

            IMAPFolder emailFolder = (IMAPFolder) emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            for (Message message : messages) {
                if (message.isSet(Flags.Flag.SEEN)) {
                    continue;
                }

                MimeMessage mimeMessage = (MimeMessage) message;
                processMessage(mimeMessage);

                message.setFlag(Flags.Flag.SEEN, true);
            }

            emailFolder.close(false);
            emailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(MimeMessage message) {
        try {
            String subject = message.getSubject();
            String content = message.getContent().toString();

            if (content.startsWith("ID: ")) {
                String postIdStr = subject.substring(13); // Extract post ID
                Long postId = Long.parseLong(postIdStr);

                postAnswerService.savePostAnswer(postId, content);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendToFrontEnd(String postId, String responseContent) {
        String message = String.format("{\"postId\": \"%s\", \"response\": \"%s\"}", postId, responseContent);
        WebSocketHandler.sendMessageToAll(message);
    }
}