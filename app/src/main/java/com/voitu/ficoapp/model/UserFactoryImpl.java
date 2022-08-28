package com.voitu.ficoapp.model;

import java.util.Collections;

public class UserFactoryImpl implements UserFactory{

    private final Long threshold = 1_000_000_000L;
    @Override
    public User createFicoMember(String name,String email, String surname, String alias) {
        return new UserImpl(name,email,
                surname, alias,
                new FicoPointsStatusImpl(Collections.emptyList(),
                        threshold), Role.MEMBER);
    }
    @Override
    public User createFicoGuest(String name,String email, String surname, String alias) {
        return new UserImpl(name,email, surname, alias,
                new FicoPointsStatusImpl(Collections.emptyList(),
                        threshold), Role.GUEST);
    }
    @Override
    public User createFicoEnemy(String name,String email, String surname, String alias) {
        return new UserImpl(name,email, surname, alias,
                new FicoPointsStatusImpl(Collections.emptyList(),
                        threshold), Role.ENEMY);
    }
    @Override
    public User createTobia(String name,String email, String surname, String alias) {
        return new UserImpl(name,email, surname, alias,
                new FicoPointsStatusImpl(Collections.emptyList(),
                        threshold), Role.PIU_TOBIA);
    }
}
