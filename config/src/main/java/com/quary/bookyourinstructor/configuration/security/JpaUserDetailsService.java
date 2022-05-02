package com.quary.bookyourinstructor.configuration.security;

import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.configuration.security.mapper.UserDetailsMapper;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService, UserStore {

    private final UserRepository userRepository;
    private final UserDetailsMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(JpaUserDetailsService::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " user not found"));
    }

    private static UserDetails buildUserDetails(UserEntity user) {
        return new User(user.getEmail(), user.getPassword(), List.of(new UserTypeAuthority(user.getType())));
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean userExists(ExternalIdentity externalIdentity) {
        return userRepository.existsByExternalIdAndExternalIdProvider(externalIdentity.getId(), externalIdentity.getProvider().name());
    }

    @Override
    public void registerUser(com.quary.bookyourinstructor.model.user.User user) throws UserWithEmailAlreadyExists {
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
}
