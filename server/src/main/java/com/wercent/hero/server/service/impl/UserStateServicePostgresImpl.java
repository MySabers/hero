package com.wercent.hero.server.service.impl;

import com.wercent.hero.server.service.UserStateService;

public class UserStateServicePostgresImpl implements UserStateService {

    @Override
    public void online(String userId, String ip) {

    }

    @Override
    public void offline(String userId) {

    }

    @Override
    public String getUserConnectIp(String userId) {
        return null;
    }

}
