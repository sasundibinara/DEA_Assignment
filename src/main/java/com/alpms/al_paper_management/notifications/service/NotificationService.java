package com.alpms.al_paper_management.notifications.service;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.notifications.model.Notification;
import com.alpms.al_paper_management.notifications.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    public List<Notification> findRecentFor(User user) {
        return notifications.findTop10ByUserOrderByCreatedAtDesc(user);
    }

    public Notification create(User user, String title, String message) {
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);
        return notifications.save(n);
    }
}


