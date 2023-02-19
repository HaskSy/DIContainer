package org.example.application.repositories;

import org.example.application.interfaces.UserRepository;
import org.example.dicontainer.annotations.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Override
    public String getUsername() {
        return "Viktor";
    }
}
