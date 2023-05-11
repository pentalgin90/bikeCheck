package com.bragin.bike_theft_check.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BikeDto {
    private String frameNumber;
    private String vendor;
    private String modelName;
    private String description;
    private Boolean wanted;
    private byte[] image;
}
