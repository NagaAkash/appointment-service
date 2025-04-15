package com.hospital.appointment.service;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

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
        when(repository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment saved = service.save(appointment);

        assertNotNull(saved);
        assertEquals(1L, saved.getPatientId());
        assertEquals("Checkup", saved.getReason());
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
    void testUpdate(){
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setPatientId(1L);
        appointment1.setAppointmentDate(LocalDateTime.of(2025, 4, 14, 10, 0));
        appointment1.setReason("Dental");
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenReturn(appointment1);
        Appointment update = service.update(1L, appointment1);
        assertNotNull(update);
        assertEquals(1L, appointment1.getPatientId());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Appointment.class));

    }
    @Test
    void testDelete_Success() {
        // Arrange
        Long validId = 1L;
        when(repository.findById(validId)).thenReturn(Optional.of(appointment));
        doNothing().when(repository).delete(any(Appointment.class));

        // Act & Assert
        assertDoesNotThrow(() -> service.delete(validId));

        // Verify
        verify(repository, times(1)).findById(validId);
        verify(repository, times(1)).delete(any(Appointment.class));
    }


}