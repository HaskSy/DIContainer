package org.example.application.services;

import org.example.application.interfaces.UserService;
import org.example.application.repositories.UserRepositoryImpl;
import org.example.dicontainer.annotations.Autowired;
import org.example.dicontainer.annotations.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getUsername() {
        return userRepository.getUsername();
    }
}
