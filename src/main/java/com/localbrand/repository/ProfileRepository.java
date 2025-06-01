package com.localbrand.repository;

import com.localbrand.model.Profile;
import java.util.List;

public interface ProfileRepository {
    List<Profile> findAll();
    Profile save(Profile profile);
} 