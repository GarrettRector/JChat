package com.muc;

public interface UserStatusListener {
    void online(String login);
    void offline(String login);
}
