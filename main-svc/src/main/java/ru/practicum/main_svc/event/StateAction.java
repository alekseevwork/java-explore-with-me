package ru.practicum.main_svc.event;

import java.util.Optional;
import java.util.stream.Stream;

public enum StateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<StateAction> from(String stringStateAction) {
        return Stream.of(values())
                .filter(state -> state.name().equalsIgnoreCase(stringStateAction))
                .findFirst();
    }
}