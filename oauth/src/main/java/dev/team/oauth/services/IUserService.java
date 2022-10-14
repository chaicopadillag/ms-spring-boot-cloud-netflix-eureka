package dev.team.oauth.services;

import dev.team.usercommons.models.entity.User;

public interface IUserService {
    public User findByName(String name);

    public User update(User user, Long id);
}
