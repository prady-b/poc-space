
package com.prady.sample.reactive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.prady.sample.reactive.domain.UserAccount;
import com.prady.sample.reactive.dto.UserAccountDTO;

/**
 * Created by Prady on 6/18/17.
 */
@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    @Mapping(target = "password", ignore = true)
    UserAccountDTO toUserAccountDTO(UserAccount userAccount);

    @Mapping(target = "password", ignore = true)
    UserAccount toUserAccount(UserAccountDTO userAccountDTO);

    @Mapping(target = "password", ignore = true)
    void toUserAccount(UserAccountDTO userAccountDTO, @MappingTarget UserAccount userAccount);
}
