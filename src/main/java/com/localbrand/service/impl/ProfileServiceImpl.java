package com.localbrand.service.impl;

import com.localbrand.model.Profile;
import com.localbrand.repository.ProfileRepository;
import com.localbrand.repository.impl.ProfileRepositoryImpl;
import com.localbrand.service.ProfileService;
import java.util.List;

public class ProfileServiceImpl implements ProfileService {
    private ProfileRepository profileRepository;

    public ProfileServiceImpl() {
        this.profileRepository = new ProfileRepositoryImpl();
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Override
    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }
} 