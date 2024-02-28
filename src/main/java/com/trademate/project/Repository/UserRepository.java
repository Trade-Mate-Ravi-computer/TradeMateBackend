package com.trademate.project.Repository;

import com.trademate.project.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    public UserModel findByEmail(String email);
}
