package com.synclife.studyroom.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CanceledReservationRepository extends JpaRepository<CanceledReservation, Long> {
}
