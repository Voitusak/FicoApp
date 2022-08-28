package com.voitu.ficoapp.model;

public interface UserFactory {
    public User createFicoMember(String name,String email, String surname, String alias);
    public User createFicoGuest(String name,String email, String surname, String alias);
    public User createFicoEnemy(String name,String email, String surname, String alias);
    public User createTobia(String name,String email, String surname, String alias);
}
