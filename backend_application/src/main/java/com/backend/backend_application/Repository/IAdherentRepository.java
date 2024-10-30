package com.backend.backend_application.Repository;

import com.backend.backend_application.Entites.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAdherentRepository  extends JpaRepository<Adherent,Long> {
    Optional<Adherent> findByEmail(String email);
}
