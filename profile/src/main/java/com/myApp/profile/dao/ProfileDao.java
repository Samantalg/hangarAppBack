package com.myApp.profile.dao;

import com.myApp.model.UserProfile;

import java.util.List;

public interface ProfileDao {

    UserProfile save(UserProfile profile);

    Boolean existById(UserProfile profile);

    UserProfile getProfileById(long id);

    List<UserProfile> getProfiles();
}