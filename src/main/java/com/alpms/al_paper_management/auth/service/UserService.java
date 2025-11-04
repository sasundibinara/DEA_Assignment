package com.alpms.al_paper_management.auth.service;

import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public long countAdmins() {
        return users.countByRole(User.Role.ADMIN);
    }

    public long countTeachers() {
        return users.countByRole(User.Role.TEACHER);
    }

    public long countStudents() {
        return users.countByRole(User.Role.STUDENT);
    }

    public List<User> findAll() {
        return users.findAll();
    }

    public long countAll() {
        return users.count();
    }

}
