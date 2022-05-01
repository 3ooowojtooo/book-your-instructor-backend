package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
