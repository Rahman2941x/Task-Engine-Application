package com.taskengine.taskengine_user_service.repository;

import com.taskengine.taskengine_user_service.model.OAuthClient;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthClientRepo extends JpaRepository<OAuthClient,Long> {
    Optional<OAuthClient> findByClientId(@NotNull String clientId);

    boolean existsByClientId(@NotNull @Param("clientId") String clientId);
}
