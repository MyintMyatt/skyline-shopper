package com.orion.auth_service.repo;

import com.orion.auth_service.entity.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByRefreshToken(String refreshToken);

}
