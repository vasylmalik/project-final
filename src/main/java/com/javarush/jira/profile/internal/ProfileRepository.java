package com.javarush.jira.profile.internal;

import com.javarush.jira.common.BaseRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ProfileRepository extends BaseRepository<Profile> {
    default Profile getOrCreate(long id) {
        return findById(id).orElseGet(() -> new Profile(id));
    }
}
