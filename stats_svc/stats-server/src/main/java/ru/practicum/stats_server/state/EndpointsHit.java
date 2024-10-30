package ru.practicum.stats_server.state;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Timestamp;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
public class EndpointsHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(nullable = false)
    String app;

    @NotBlank
    @Column(nullable = false)
    String uri;

    @NotBlank
    @Column(nullable = false)
    String ip;

    @Timestamp
    @Column(nullable = false)
    LocalDateTime timestamp;
}
