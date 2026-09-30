package com.dvh.vendor.entity;

import com.dvh.user.entity.User;
import com.dvh.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "vendors",
       uniqueConstraints ={
        @UniqueConstraint(
                name = "uk_vendor_gst_number",columnNames = "gst_number"
        )})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "vendor",fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();

    @Column(name = "business_name",nullable = false, length = 100)
    private String businessName;

    @Column(name = "business_address",nullable = false,length = 255)
    private String businessAddress;

    @Column(nullable = false,length = 30)
    private String city;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$", message = "Invalid GST number")
    @Column(name = "gst_number", length = 15, unique = true)
    private String gstNumber;

    @Builder.Default
    @Column(nullable = false)
    private Boolean verified = false;
}
