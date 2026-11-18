package com.nibm.echannelling.echannelingapplication.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String email;
    private String name;
    private String specialty;
    private String availability;
    private Boolean verified;
    private String phone;
    private Integer yearsOfExperience;
    private String bio;
    private String education;

}
