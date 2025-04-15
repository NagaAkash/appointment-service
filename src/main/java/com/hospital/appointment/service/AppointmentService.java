package com.hospital.appointment.service;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    public Appointment save(Appointment appointment) {
        return repository.save(appointment);
    }

    public Appointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    public List<Appointment> findAll() {
        return repository.findAll();
    }
    public Appointment update(long id,Appointment appointment){
        Optional<Appointment> byId = repository.findById(id);
        Appointment appointment1 = byId.get();
        appointment1.setPatientId(appointment.getPatientId());
        appointment1.setAppointmentDate(appointment.getAppointmentDate());
        appointment1.setReason(appointment.getReason());
        return repository.save(appointment1);
    }

    public void delete(Long id) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        repository.delete(existing);
    }


}