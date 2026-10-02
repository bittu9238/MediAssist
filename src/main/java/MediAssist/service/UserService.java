package MediAssist.service;

import MediAssist.model.User;
import MediAssist.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository) {

        this.userRepository =
                userRepository;
    }

    public User registerUser(
            String name,
            String email,
            String password) {

        if (userRepository.existsByEmail(email)) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user =
                new User(
                        name,
                        email,
                        password
                );

        return userRepository.save(user);
    }

    public User loginUser(
            String email,
            String password) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        if (!user.getPassword().equals(password)) {

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        return user;
    }
}
