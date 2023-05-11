package com.bragin.bike_theft_check.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
