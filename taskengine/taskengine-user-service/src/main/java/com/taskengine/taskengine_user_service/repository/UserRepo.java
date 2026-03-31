package com.taskengine.taskengine_user_service.repository;

import com.taskengine.taskengine_user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long>{

    Optional<User> findByUserName(String userName);

    List<User> findByEmailIn(List<String> email);

    Optional<User> findByEmail(String email);
}
