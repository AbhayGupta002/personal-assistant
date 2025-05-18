package com.personal.assistant.repository;

import com.personal.assistant.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLoginRepository extends JpaRepository<UserLogin , String>{
    @Query(
    value = "SELECT count(*) FROM user_login u WHERE u.email = :email AND u.password = :password",
    nativeQuery = true
)
int findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
