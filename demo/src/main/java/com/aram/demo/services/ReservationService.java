package com.aram.demo.services;

import com.aram.demo.models.Reservation;
import com.aram.demo.utils.HibernateUtils;
import com.google.inject.Singleton;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class ReservationService implements IReservationService {
    private final int maximumReservationDays = 3;
    ReentrantLock lock = new ReentrantLock();

    @Override
    public void cancelReservation(Reservation reservation) {
        lock.lock();
        HibernateUtils.deleteEntity(reservation);
        lock.unlock();
    }

    @Override
    public Reservation addOrUpdateReservation(Reservation reservation, boolean updateMode) {
        lock.lock();
        DateTime startDate = reservation.getReservationStartDate();
        DateTime endDate = reservation.getReservationEndDate();

        endDate = endDate.withHourOfDay(12);
        endDate = endDate.withMinuteOfHour(0);
        endDate = endDate.withSecondOfMinute(0);
        startDate = startDate.withHourOfDay(12);
        startDate = startDate.withMinuteOfHour(1);
        startDate = startDate.withSecondOfMinute(0);

        List<Reservation> otherReservations = getReservations(startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Reservation starting date must be before it's end date.");
        } else if ((endDate.getMillis() - startDate.getMillis()) > maximumReservationDays * 24 * 60 * 60 * 1000) { //convert to milliseconds
            throw new RuntimeException("Reservations longer than 3 days are not currently supported.");
        } else if (!otherReservations.isEmpty()) {
            throw new RuntimeException("At least one of the selected days is not available.");
        } else if (startDate.isBefore(DateTime.now().plusDays(1))) {
            throw new RuntimeException("Invalid date. Unable to make reservations for past days or within the next 24 hours");
        } else if (endDate.isAfter(DateTime.now().plusMonths(1))) {
            throw new RuntimeException("Invalid date. Unable to make reservations for days further than a month away");
        }

        reservation.setReservationEndDate(endDate);
        reservation.setReservationStartDate(startDate);

        if (updateMode) {
            HibernateUtils.updateEntity(reservation);
            lock.unlock();
            return reservation;
        } else {
            lock.unlock();
            return (Reservation) HibernateUtils.createEntity(reservation);
        }
    }

    @Override
    public Reservation getReservationByBookingId(String bookingId) {
        Object entity = HibernateUtils.getEntityByProperty(Reservation.class, "bookingId", bookingId);
        if (entity == null) {
            throw new RuntimeException("Invalid bookingId");
        }
        return (Reservation) entity;
    }

    @Override
    public List<Reservation> getReservations(DateTime startDate, DateTime endDate) {
        List<SimpleExpression> simpleExpressions = new ArrayList<>();
        simpleExpressions.add(Restrictions.lt("reservationStartDate", endDate));
        simpleExpressions.add(Restrictions.ge("reservationEndDate", startDate));
        return HibernateUtils.getEntityListWithRestrictions(Reservation.class, simpleExpressions);
    }

    @Override
    public List<String> getAvailableDates(DateTime startDate, DateTime endDate) {
        List<String> availableDates = new ArrayList<>();
        List<Reservation> reservationList = getReservations(startDate, endDate);

        endDate = endDate.withHourOfDay(12);
        endDate = endDate.withMinuteOfHour(0);
        endDate = endDate.withSecondOfMinute(0);
        startDate = startDate.withHourOfDay(12);
        startDate = startDate.withMinuteOfHour(1);
        startDate = startDate.withSecondOfMinute(0);

        DateTime currentDate = startDate;

        currentDate = currentDate.withHourOfDay(12);
        currentDate = currentDate.withMinuteOfHour(1);
        currentDate = currentDate.withSecondOfMinute(1);

        while (currentDate.isBefore(endDate)) {
            boolean available = true;

            for (Reservation reservation : reservationList) {
                if ((currentDate.isBefore(reservation.getReservationEndDate()) && currentDate.isAfter(reservation.getReservationStartDate()))) {
                    available = false;
                    break;
                }
            }
            if (available) {
                availableDates.add(DateTimeFormat.forPattern("yyyy/MM/dd").print(currentDate));
            }
            currentDate = currentDate.plusDays(1);
        }
        return availableDates;
    }
}
