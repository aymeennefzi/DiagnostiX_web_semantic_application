package com.backend.backend_application.Repository;


import com.backend.backend_application.Entites.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdminRepository extends JpaRepository<Admin,Long> {
}
