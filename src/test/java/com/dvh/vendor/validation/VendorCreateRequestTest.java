package com.dvh.vendor.validation;


import com.dvh.vendor.dto.request.VendorCreateRequest;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class VendorCreateRequestTest {

    private static Validator validator;
    private static  ValidatorFactory validatorFactory;


    @BeforeAll
    static void setUp(){
         validatorFactory = Validation.buildDefaultValidatorFactory();
         validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown(){
        validatorFactory.close();
    }



    @Test
    void shouldRejectVendorRequest_whenBusinessNameIsBlank(){
        VendorCreateRequest vendorCreateRequest = new VendorCreateRequest(
                "",
                "Bhawarkuan",
                "INDORE",
                "GST123");

        Set<ConstraintViolation<VendorCreateRequest>> validations = validator.validate(vendorCreateRequest);

        assertFalse(validations.isEmpty());

        assertTrue(validations.stream()
                .anyMatch(
                        v-> v.getPropertyPath().toString().equals("businessName")));

    }

    @Test
    void shouldRejectVendorRequest_whenBusinessNameIsTooShort(){

        VendorCreateRequest request = new VendorCreateRequest(
                "ad",
                "Bhawarkuan",
                "INDORE",
                "GST124");

        Set<ConstraintViolation<VendorCreateRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());

        assertTrue(validate.stream().anyMatch(
                v->v.getPropertyPath().toString().equals("businessName")));
    }

    @Test
    void shouldRejectVendorRequest_whenBusinessNameIsTooLong(){

        String businessName = "A".repeat(101);

        VendorCreateRequest request = new VendorCreateRequest(
                businessName,
                "Bhawarkuan",
                "INDORE",
                "GST123");

        Set<ConstraintViolation<VendorCreateRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());

        assertTrue(validate.stream().anyMatch(
                n->n.getPropertyPath().toString().equals("businessName")));
    }

    @Test
    void shouldRejectVendorRequest_whenVendorBusinessAddressIsBlank(){
        VendorCreateRequest request = VendorCreateRequest.builder()
                .businessName("Hello Car")
                .businessAddress("")
                .city("INDORE")
                .gstNumber("123ABC")
                .build();

        Set<ConstraintViolation<VendorCreateRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());

        assertTrue(validate.stream().anyMatch(v->v.getPropertyPath().toString().equals("businessAddress")));

    }

    @Test
    void shouldRejectVendorRequest_whenVendorsBusinessAddressIsTooLong(){
        String businessAddress = "A".repeat(256);

        VendorCreateRequest request = VendorCreateRequest.builder()
                .businessName("Hello Car")
                .businessAddress(businessAddress)
                .city("INDORE")
                .gstNumber("ABC124")
                .build();

        Set<ConstraintViolation<VendorCreateRequest>> validation = validator.validate(request);

        assertFalse(validation.isEmpty());

        assertTrue(validation.stream().anyMatch(v->v.getPropertyPath().toString().equals("businessAddress")));
    }
}
