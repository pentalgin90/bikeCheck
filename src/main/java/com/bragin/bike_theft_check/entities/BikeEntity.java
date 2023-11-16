package com.bragin.bike_theft_check.entities;

import com.bragin.bike_theft_check.model.states.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "thefted_bikes")
public class BikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "frame_number")
    private String frameNumber;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "date_created")
    @Timestamp
    private LocalDateTime dateCreated;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "link")
    private String link;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
