package com.ficklebytes.entity;

import com.ficklebytes.enums.ActivityName;
import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "target_id")
    private int targetId;

    @Column(name = "activity")
    @Enumerated(EnumType.STRING)
    private ActivityName activity;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
