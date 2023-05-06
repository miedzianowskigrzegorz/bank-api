package pl.gm.bankapi.notification.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.gm.bankapi.notification.dto.NotificationDto;
import pl.gm.bankapi.notification.model.NotificationEntity;
import pl.gm.bankapi.notification.repository.NotificationRepository;
import pl.gm.bankapi.user.model.User;
import pl.gm.bankapi.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**

 Service responsible for managing notifications for users.
 */
@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.modelMapper = modelMapper;
    }

    /**
     Adds a new notification to the given user's notification list.
     @param username the username of the user
     @param message the message to be added as a notification
     */
    public void addNotificationToUser(String username, String message) {
        User user = userRepository.getUserByUsername(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now().format(formatter));
        notification.setUser(user);

        notificationRepository.save(notification);
    }

    /**
     Retrieves a list of notifications for the given user.
     @param user the user to retrieve notifications for
     @return a list of notification DTOs
     */
    public List<NotificationDto> getNotificationsForUser(User user) {
        List<NotificationEntity> notificationDtos = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notificationDtos.stream()
                .map(n -> modelMapper.map(n, NotificationDto.class)).collect(Collectors.toList());
    }
}