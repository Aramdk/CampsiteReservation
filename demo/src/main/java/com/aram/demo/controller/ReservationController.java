package com.aram.demo.controller;

import com.aram.demo.Responses.ObjectResponse;
import com.aram.demo.models.Reservation;
import com.aram.demo.models.User;
import com.aram.demo.services.IReservationService;
import com.aram.demo.services.IUserService;
import com.aram.demo.services.ReservationService;
import com.aram.demo.services.UserService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api")
public class ReservationController {

    IUserService userService = new UserService();
    IReservationService reservationService = new ReservationService();
    ReentrantLock lock = new ReentrantLock();

    @RequestMapping(value = "/reservation", method = RequestMethod.DELETE)
    public void cancelReservation(String bookingId) {
        lock.lock();
        reservationService.cancelReservation(reservationService.getReservationByBookingId(bookingId));
        lock.unlock();
    }

    @RequestMapping(value = "/reservations", method = RequestMethod.GET)
    public ObjectResponse getReservations(Date startDate, Date endDate) {
        lock.lock();
        ObjectResponse response = new ObjectResponse();
        try {
            DateTime startDateTime = new DateTime(startDate);
            DateTime endDateTime = new DateTime(endDate);
            if (endDate == null) {
                endDateTime = startDateTime.plusMonths(1);
            }
            response.setResponseBody(reservationService.getReservations(startDateTime, endDateTime));
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setErrorMessage(e.getMessage());
        }
        lock.unlock();
        return response;
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.GET)
    public ObjectResponse getReservation(String bookingId) {
        lock.lock();
        ObjectResponse response = new ObjectResponse();
        try {
             response.setResponseBody(reservationService.getReservationByBookingId(bookingId).toApiModel());
             response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setErrorMessage(e.getMessage());
        }
        lock.unlock();
        return response;
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.POST)
    public ObjectResponse addReservation(String username, String userEmail, String startDate, String endDate) {
        lock.lock();
        ObjectResponse response = new ObjectResponse();
        DateTime startDateTime = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy/MM/dd"));
        DateTime endDateTime = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy/MM/dd"));
        try {
            User user = userService.getUserByEmail(userEmail);
            if (user == null) {
                user = userService.addUser(new User(username, userEmail));
            }
            response.setResponseBody(reservationService.addOrUpdateReservation(new Reservation(user, startDateTime, endDateTime), false).toApiModel());
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setErrorMessage(e.getMessage());
        }
        lock.unlock();
        return response;
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.PUT)
    public ObjectResponse updateReservation(String bookingId, String startDate, String endDate) {
        lock.lock();
        ObjectResponse response = new ObjectResponse();

        try {
            DateTime startDateTime = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy/MM/dd"));
            DateTime endDateTime = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy/MM/dd"));
            Reservation reservation = reservationService.getReservationByBookingId(bookingId);
            reservation.setReservationStartDate(startDateTime);
            reservation.setReservationEndDate(endDateTime);
            response.setResponseBody(reservationService.addOrUpdateReservation(reservation, true).toApiModel());
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setErrorMessage(e.getMessage());
        }

        lock.unlock();
        return response;
    }

    @RequestMapping(value = "/available-dates", method = RequestMethod.GET)
    public ObjectResponse getAvailableDates(Date startDate, Date endDate) {
        lock.lock();
        ObjectResponse response = new ObjectResponse();
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        try {
            if (endDate == null) {
                endDateTime = startDateTime.plusMonths(1);
            }
            response.setResponseBody(reservationService.getAvailableDates(startDateTime, endDateTime));
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(400);
            response.setErrorMessage(e.getMessage());
        }
        lock.unlock();
        return response;
    }
}
