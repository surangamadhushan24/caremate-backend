package com.nibm.echannelling.echannelingapplication.mapper;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "user.email", target = "email")
    DoctorDTO toDto(Doctor doctor);

    List<DoctorDTO> toDtoList(List<Doctor> doctors);
}