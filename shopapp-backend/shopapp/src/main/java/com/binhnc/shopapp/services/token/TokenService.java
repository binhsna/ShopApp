package com.binhnc.shopapp.services.token;

import com.binhnc.shopapp.components.JwtTokenUtils;
import com.binhnc.shopapp.exceptions.DataNotFoundException;
import com.binhnc.shopapp.models.Token;
import com.binhnc.shopapp.models.User;
import com.binhnc.shopapp.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional
    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        // Số lượng token vượt quá giới hạn, xóa cột token cũ
        if (tokenCount >= MAX_TOKENS) {
            // Kiểm tra trong danh sách userTokens có tồn tại ít nhất
            // 1 token không phải là thiết bị di động (non-mobile)
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                // Tất cả các token đều là thiết bị di động -> Xóa token đầu tiên trong list
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        //=>
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        // Tạo mới 1 token cho người dùng
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token refreshToken(String refreshToken, User user) throws Exception {
        Token exsitingToken = tokenRepository.findByRefreshToken(refreshToken);
        if (exsitingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        String newToken = jwtTokenUtils.generateToken(exsitingToken.getUser());
        exsitingToken.setToken(newToken);
        exsitingToken.setExpirationDate(LocalDateTime.now().plusSeconds(expiration));
        exsitingToken.setRefreshToken(UUID.randomUUID().toString());
        exsitingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(exsitingToken);
        return exsitingToken;
    }
}
