package com.bookhub.dal;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
