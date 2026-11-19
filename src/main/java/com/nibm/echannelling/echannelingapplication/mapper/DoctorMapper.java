package com.nibm.echannelling.echannelingapplication.mapper;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DoctorMapper {

    @Mapping(source = "user.email", target = "email")
    DoctorDTO toDto(Doctor doctor);

    List<DoctorDTO> toDtoList(List<Doctor> doctors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "availability", ignore = true)
    void updateEntityFromDto(DoctorDTO dto, @MappingTarget Doctor doctor);
}