package com.sreesubh.yaahappiness20;

import java.util.List;

public class UserListType {
    List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserListType() {
    }

    public UserListType(List<User> users) {
        this.users = users;
    }
}