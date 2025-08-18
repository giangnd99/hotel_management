package com.poly.notification.management.repository;

import com.poly.notification.management.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Notification, Integer> {
}
