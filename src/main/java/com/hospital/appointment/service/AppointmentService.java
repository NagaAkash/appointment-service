package com.hospital.appointment.service;

import com.hospital.appointment.client.PatientClient;
import com.hospital.appointment.dtos.Patient;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private PatientClient patientClient;

    public Appointment save(Appointment appointment) {
        try {
            patientClient.getPatient(appointment.getPatientId());
            logger.info("Patient ID {} validated", appointment.getPatientId());
        } catch (Exception e) {
            logger.error("Invalid patient ID: {}", appointment.getPatientId());
            throw new IllegalArgumentException("Patient not found for ID: " + appointment.getPatientId());
        }
        return repository.save(appointment);
    }

    public Appointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }


    public List<Appointment> findAll() {
        return repository.findAll();
    }

    public Appointment update(Long id, Appointment updated) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        try {
            patientClient.getPatient(updated.getPatientId());
            logger.info("Patient ID {} validated for update", updated.getPatientId());
        } catch (Exception e) {
            logger.error("Invalid patient ID: {}", updated.getPatientId());
            throw new IllegalArgumentException("Patient not found for ID: " + updated.getPatientId());
        }
        existing.setPatientId(updated.getPatientId());
        existing.setAppointmentDate(updated.getAppointmentDate());
        existing.setReason(updated.getReason());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        repository.delete(existing);
    }
  public Appointment getOpName(String firstName){
      Patient patient = patientClient.getPatientByName(firstName);
      if(patient != null && patient.getFirstName().equalsIgnoreCase(firstName)){
          Optional<Appointment> byPatientId = repository.findByPatientId(patient.getId());
          return byPatientId.orElseThrow(() -> new RuntimeException("No appointment found for patient: " + firstName));
      }

      throw new RuntimeException("No patient found with name: " + firstName);
  }

}