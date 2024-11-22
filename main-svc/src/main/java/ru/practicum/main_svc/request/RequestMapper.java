package ru.practicum.main_svc.request;

import lombok.experimental.UtilityClass;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        if (request == null) {
            return null;
        }
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }
}
