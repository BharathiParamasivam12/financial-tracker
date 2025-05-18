package com.finance.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finance.user.entity.UserInfoEntity;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
}
