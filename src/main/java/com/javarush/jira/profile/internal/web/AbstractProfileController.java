package com.javarush.jira.profile.internal.web;

import com.javarush.jira.common.util.validation.ValidationUtil;
import com.javarush.jira.profile.ProfileDTO;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.ProfileUtil;
import com.javarush.jira.profile.internal.model.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractProfileController {
    @Autowired
    protected ProfileMapper profileMapper;
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO get(long id) {
        log.info("get {}", id);
        return profileMapper.toDTO(profileRepository.getOrCreate(id));
    }

    public void update(ProfileDTO profileDTO, long id) {
        log.info("update {}, user {}", profileDTO, id);
        ValidationUtil.assureIdConsistent(profileDTO, id);
        ValidationUtil.assureIdConsistent(profileDTO.getContacts(), id);
        ProfileUtil.checkContactsExist(profileDTO.getContacts());
        Profile profile = profileMapper.updateFromDTO(profileRepository.getOrCreate(profileDTO.id()), profileDTO);
        profileRepository.save(profile);
    }
}
