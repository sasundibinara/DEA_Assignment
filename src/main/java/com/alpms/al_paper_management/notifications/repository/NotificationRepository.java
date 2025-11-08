package com.alpms.al_paper_management.notifications.repository;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Recent notifications for a given user
    List<Notification> findTop10ByUserOrderByCreatedAtDesc(User user);
}
