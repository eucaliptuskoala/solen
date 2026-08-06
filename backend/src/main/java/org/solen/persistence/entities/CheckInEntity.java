package org.solen.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="check_ins",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"practice_id", "date"})
        }
)
public class CheckInEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "practice_id", nullable = false)
    private PracticeEntity practice;

    @Column(name="date", nullable = false)
    private LocalDate date;

    @Column(name = "streak_value", nullable = false)
    private int streakValue;

    @Column(name = "content")
    private String content;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "mood")
    private String mood;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
}
