package com.javarush.jira.profile.internal;

import com.javarush.jira.profile.ContactDTO;
import com.javarush.jira.profile.ProfileDTO;
import com.javarush.jira.profile.internal.model.Contact;
import com.javarush.jira.profile.internal.model.Profile;
import com.javarush.jira.profile.internal.web.ProfilePostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "mailNotifications", expression = "java(ProfileUtil.maskToNotifications(entity.getMailNotifications()))")
    ProfileDTO toDTO(Profile entity);

    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "mailNotifications", expression = "java(ProfileUtil.notificationsToMask(dto.getMailNotifications()))")
    Profile updateFromDTO(@MappingTarget Profile entity, ProfileDTO dto);

    Contact toContact(ContactDTO contact);

    @Mapping(target = "contacts", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    ProfileDTO fromPostToDTO(ProfilePostRequest profilePostRequest);
}
