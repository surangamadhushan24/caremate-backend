package com.nibm.echannelling.echannelingapplication.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {

    private String name;
    private String email;
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
