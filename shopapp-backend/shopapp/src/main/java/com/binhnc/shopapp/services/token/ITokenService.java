package com.binhnc.shopapp.services.token;

import com.binhnc.shopapp.models.Token;
import com.binhnc.shopapp.models.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);

    Token refreshToken(String refreshToken, User user) throws Exception;
}

