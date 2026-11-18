package com.nibm.echannelling.echannelingapplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private Integer age;
    private String gender;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String bloodType;
    private Integer height;
    private Integer weight;
    private String symptoms;
}
