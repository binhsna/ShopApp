package com.binhnc.shopapp.services.token;

import com.binhnc.shopapp.models.User;

public interface ITokenService {
    void addToken(User user, String token, boolean isMobileDevice);
}

