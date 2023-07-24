package com.bragin.bike_theft_check.configuration;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueMappingStrategy =  NullValueMappingStrategy.RETURN_DEFAULT
)
public interface MapStructConfig {
}
