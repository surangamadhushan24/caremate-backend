package com.nibm.echannelling.echannelingapplication.mapper;



import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.entity.Patient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO toDto(Patient patient);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)

    void updateFromRequest(UpdatePatientRequest request, @MappingTarget Patient patient);
}