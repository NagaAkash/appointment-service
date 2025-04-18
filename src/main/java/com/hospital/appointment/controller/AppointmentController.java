package com.hospital.appointment.controller;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment create(@Valid @RequestBody Appointment appointment) {
        logger.info("Creating appointment for patient ID: {}", appointment.getPatientId());
        return service.save(appointment);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Appointment getById(@PathVariable Long id) {
        logger.info("Fetching appointment with ID: {}", id);
        return service.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Appointment> getAll() {
        logger.info("Fetching all appointments");
        return service.findAll();
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  Appointment update (@PathVariable long id ,@Valid @RequestBody Appointment appointment){

        logger.info("Updated appointment with ID: {}", id);
        return service.update(id, appointment);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id){
        logger.info("Deleted the appointment with ID: {}",id);
        service.delete(id);
    }
    @GetMapping("/by-name/{firstName}")
    @ResponseStatus(HttpStatus.OK)
    public Appointment getByName(@PathVariable("firstName")String firstName){
        Appointment opName = service.getOpName(firstName);
                return opName;
    }

}