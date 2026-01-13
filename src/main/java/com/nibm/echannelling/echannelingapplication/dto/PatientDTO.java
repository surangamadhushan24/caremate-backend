package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String email;
    private String name;
    private Integer age;
    private String gender;
    private String symptoms;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String bloodType;
    private Integer height;
    private Integer weight;
}