package com.aram.demo.services;

import com.aram.demo.models.Reservation;
import org.joda.time.DateTime;

import java.util.List;

public interface IReservationService {
    boolean validateCampsiteAvailability(DateTime reservationStartDate, int reservationDuration);

    void cancelReservation(Reservation reservation);

    Reservation addReservation(Reservation reservation);

    Reservation getReservationByBookingId(String bookingId);

    List<Reservation> getReservations(DateTime startDate, DateTime endDate);

    List<String> getAvailableDates(DateTime startDate, DateTime endDate);
}