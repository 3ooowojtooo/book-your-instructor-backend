package com.quary.bookyourinstructor.configuration.security.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface UserDetailsMapper {

    @Mapping(target = "externalId", source = "externalIdentity.id")
    @Mapping(target = "externalIdProvider", source = "externalIdentity.provider")
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "eventRealizations", ignore = true)
    UserEntity mapToEntity(User user);

    @Mapping(target = "externalIdentity.id", source = "externalId")
    @Mapping(target = "externalIdentity.provider", source = "externalIdProvider")
    User mapToModel(UserEntity userEntity);
}
