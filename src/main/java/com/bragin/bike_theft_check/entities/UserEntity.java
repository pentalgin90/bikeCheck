package com.bragin.bike_theft_check.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bike_user")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private long userId;
    @OneToMany(mappedBy = "userEntity")
    private List<BikeEntity> bikeEntityList = new ArrayList<>();
}
