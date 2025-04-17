package com.hospital.appointment.service;

import com.hospital.appointment.client.PatientClient;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private PatientClient patientClient;

    @InjectMocks
    private AppointmentService service;

    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(LocalDateTime.of(2025, 4, 14, 10, 0));
        appointment.setReason("Checkup");
    }

    @Test
    void testSave() {
        when(patientClient.getPatient(anyLong())).thenReturn(new Object());
        when(repository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment saved = service.save(appointment);

        assertNotNull(saved);
        assertEquals(1L, saved.getPatientId());
        assertEquals("Checkup", saved.getReason());
        verify(patientClient, times(1)).getPatient(1L);
        verify(repository, times(1)).save(appointment);
    }

    @Test
    void testFindById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment found = service.findById(1L);

        assertNotNull(found);
        assertEquals(LocalDateTime.of(2025, 4, 14, 10, 0), found.getAppointmentDate());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.findById(999L);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void testUpdate_Success() {
        Appointment existing = new Appointment();
        existing.setId(1L);
        existing.setPatientId(1L);
        existing.setAppointmentDate(LocalDateTime.of(2025, 4, 14, 9, 0));
        existing.setReason("Checkup");

        Appointment updated = new Appointment();
        updated.setId(1L);
        updated.setPatientId(2L);
        updated.setAppointmentDate(LocalDateTime.of(2025, 4, 14, 10, 0));
        updated.setReason("Dental");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientClient.getPatient(anyLong())).thenReturn(new Object());
        when(repository.save(any(Appointment.class))).thenReturn(updated);

        Appointment result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals(2L, result.getPatientId());
        assertEquals(LocalDateTime.of(2025, 4, 14, 10, 0), result.getAppointmentDate());
        assertEquals("Dental", result.getReason());
        verify(repository, times(1)).findById(1L);
        verify(patientClient, times(1)).getPatient(2L);
        verify(repository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testDelete_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        doNothing().when(repository).delete(any(Appointment.class));

        service.delete(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(appointment);
    }

    @Test
    void testDelete_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.delete(999L);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(repository, times(1)).findById(999L);
        verify(repository, never()).delete(any());
    }
}