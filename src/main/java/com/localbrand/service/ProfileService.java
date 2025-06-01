package com.localbrand.service;

import com.localbrand.model.Profile;
import java.util.List;

public interface ProfileService {
    List<Profile> getAllProfiles();
    Profile createProfile(Profile profile);
} 