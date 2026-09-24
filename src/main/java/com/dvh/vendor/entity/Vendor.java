package com.dvh.vendor.entity;

import com.dvh.user.entity.User;
import com.dvh.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "vendors",
       uniqueConstraints ={
        @UniqueConstraint(
                name = "uk_vendor_gst_number",columnNames = "gst_number"
        )})
@Data
@NoArgsConstructor@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @OneToMany(mappedBy = "vendor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();

    @Column(name = "business_name",nullable = false, length = 100)
    private String businessName;

    @Column(name = "business_address",nullable = false,length = 255)
    private String businessAddress;

    @Column(nullable = false,length = 30)
    private String city;

    @Column(name = "gst_number",length = 15,unique = true)
    private String gstNumber;

    @Column(nullable = false)
    @Builder.Default
    private Boolean verified = false;
}
