package com.cloud.coffee.service;

import com.cloud.coffee.domain.User;
import com.cloud.coffee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 포인트 조회
     */
    public Long getPoint(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return user.getPoint();
    }

    /**
     * 포인트 충전
     */
    @Transactional
    public void chargePoint(Long userId, Long amount) {
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        user.chargePoint(amount);
    }
}