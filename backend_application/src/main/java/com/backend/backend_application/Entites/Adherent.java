package com.backend.backend_application.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="Adherent")
public class Adherent  extends User  {
    @Column(name="cin")
    private long cin;

    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Column(name="image")
    private String image;
    private String verificationCode;
    private boolean verified = false;
}
