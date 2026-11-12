package com.dvh.vehicle.entity;

import com.dvh.common.entity.BaseEntity;
import com.dvh.vehicle.constants.FuelType;
import com.dvh.vehicle.constants.TransmissionType;
import com.dvh.vehicle.constants.VehicleType;
import com.dvh.vendor.entity.Vendor;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicles",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehicle_number",columnNames = "vehicle_number")})
public class Vehicle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "vendor_id",nullable = false)
    private Vendor vendor;

    @Column(name = "vehicle_number",nullable = false,length = 10)
    private String vehicleNumber;

    @Column(name = "brand",length = 20)
    private String brand;

    @Column(name = "model",length = 20)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type",nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type",nullable = false)
    private TransmissionType transmissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type",nullable = false)
    private FuelType fuelType;

    @Column(name = "manufacturing_year",nullable = false)
    private Integer manufacturingYear;

    @Column(name = "seating_capacity",nullable = false)
    private Integer seatingCapacity;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

}
