package com.quary.bookyourinstructor.configuration.security.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.model.user.UserOrigin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface UserDetailsMapper {

    @Mapping(target = "externalId", source = "externalIdentity.id")
    @Mapping(target = "externalIdProvider", source = "externalIdentity.provider")
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "eventRealizations", ignore = true)
    @Mapping(target = "absences", ignore = true)
    UserEntity mapToEntity(User user);

    @Mapping(target = "externalIdentity", source = "userEntity")
    User mapToModel(UserEntity userEntity);

    default ExternalIdentity mapToExternalIdentity(UserEntity userEntity) {
        if (userEntity.getOrigin() == UserOrigin.CREDENTIALS)
            return null;

        return new ExternalIdentity(userEntity.getExternalId(), userEntity.getExternalIdProvider());
    }
}
