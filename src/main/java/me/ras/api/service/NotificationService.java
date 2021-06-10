package me.ras.api.service;

public interface NotificationService {
    void sendNotifications();
    void publishMessageToSnsDirectly(String message);
}
