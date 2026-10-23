package com.nibm.echannelling.echannelingapplication.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    private String name;
    private Integer age;
    private String gender;
    private String symptoms;


}
