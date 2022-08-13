package com.quary.bookyourinstructor.configuration.security;

import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.configuration.security.mapper.UserDetailsMapper;
import com.quary.bookyourinstructor.configuration.security.model.UserPrincipal;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService, UserStore {

    private final UserRepository userRepository;
    private final UserDetailsMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " user not found"));
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(User user) throws UserWithEmailAlreadyExists {
        if (userExists(user.getEmail())) {
            throw new UserWithEmailAlreadyExists(user.getEmail());
        }
        final UserEntity userEntity = mapper.mapToEntity(user);
        userRepository.save(userEntity);
    }

    @Override
    public Optional<com.quary.bookyourinstructor.model.user.User> getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::mapToModel);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id)
                .map(mapper::mapToModel);
    }

    @Override
    public Optional<User> getByExternalIdentity(ExternalIdentity externalIdentity) {
        return userRepository.findByExternalIdAndExternalIdProvider(externalIdentity.getId(), externalIdentity.getProvider())
                .map(mapper::mapToModel);
    }
}
