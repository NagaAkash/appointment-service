package com.hospital.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "http://localhost:8080")
public interface PatientClient {
    @GetMapping("/patients/{id}")
    Object getPatient(@PathVariable("id") Long id);
}