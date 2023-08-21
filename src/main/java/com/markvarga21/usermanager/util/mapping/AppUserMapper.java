package com.markvarga21.usermanager.util.mapping;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * A utility class which is used for mapping between
 * the application's user entities and DTOs and backwards.
 */
@Component
@RequiredArgsConstructor
public class AppUserMapper {
    /**
     * A model mapper.
     */
    private final ModelMapper mapper;

    /**
     * Maps an {@code AppUserDto} to an {@code AppUser} entity.
     *
     * @param appUserDto the DTO object to be mapped to an entity class.
     * @return the converted {@code AppUser} entity.
     */
    public AppUser mapAppUserDtoToEntity(final AppUserDto appUserDto) {
        return this.mapper.map(appUserDto, AppUser.class);
    }

    /**
     * Maps an {@code AppUser} entity to an {@code AppUserDto}.
     *
     * @param appUser the entity object to be mapped to a DTO class.
     * @return the converted {@code AppUserDto}.
     */
    public AppUserDto mapAppUserEntityToDto(final AppUser appUser) {
        return this.mapper.map(appUser, AppUserDto.class);
    }
}
