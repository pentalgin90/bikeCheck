package com.bragin.bike_theft_check.dto;

import com.bragin.bike_theft_check.model.states.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BikeDto {
    private UUID id;
    private String frameNumber;
    private String vendor;
    private String modelName;
    private String description;
    private Status status;
    private String link;
    private Long userId;
}
