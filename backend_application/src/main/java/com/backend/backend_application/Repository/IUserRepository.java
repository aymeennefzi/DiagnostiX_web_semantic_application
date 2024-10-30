package com.backend.backend_application.Repository;


import com.backend.backend_application.Entites.Role;
import com.backend.backend_application.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);
    Optional<User> findByPasswordResetToken(String passwordResetToken);


}
