package com.nibm.echannelling.echannelingapplication.mapper;

import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.dto.AppointmentResponseDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "meetingLink", ignore = true)
    @Mapping(source = "date", target = "date", qualifiedByName = "stringToLocalDateTime")
    Appointment toEntity(AppointmentDTO dto);


    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.name", target = "doctorName")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.name", target = "patientName")
    AppointmentResponseDTO toResponseDto(Appointment entity);

    List<AppointmentResponseDTO> toResponseDtoList(List<Appointment> entities);

    // ---------- Helpers ----------
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }
}