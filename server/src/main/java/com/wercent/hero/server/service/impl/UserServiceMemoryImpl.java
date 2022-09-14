package com.wercent.hero.server.service.impl;

import com.wercent.hero.server.service.UserService;
import com.wercent.hero.server.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceMemoryImpl implements UserService {

    private final Session session;

    private final Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("刘威", "123");
        allUserMap.put("朱思源", "123");
        allUserMap.put("宋亮", "123");
        allUserMap.put("崔婷", "123");
        allUserMap.put("方鑫泽", "123");
        allUserMap.put("于云鹏", "123");
        allUserMap.put("赵策", "123");
    }

    public UserServiceMemoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public boolean login(String username, String password) {
        String pass = allUserMap.get(username);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }

    @Override
    public Map<String, String> getUsersState() {
        HashMap<String, String> userStateMapper = new HashMap<>();
        List<String> names = session.getNames();
        for (String name : names) {
            userStateMapper.put(name, "up");
        }
        allUserMap.forEach((name, password) -> userStateMapper.putIfAbsent(name, "down"));
        return userStateMapper;
    }
}