package com.nibm.echannelling.echannelingapplication.mapper;

import com.nibm.echannelling.echannelingapplication.dto.VideoCallResponse;
import com.nibm.echannelling.echannelingapplication.entity.VideoCall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VideoCallMapper {

    @Mapping(source = "id", target = "videoCallId")
    VideoCallResponse toResponse(VideoCall entity);
}