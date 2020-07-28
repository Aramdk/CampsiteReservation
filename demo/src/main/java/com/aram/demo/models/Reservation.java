package com.aram.demo.models;

import com.aram.demo.Responses.ObjectResponse;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "reservation_start_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime reservationStartDate;

    @Column(name = "reservation_end_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime reservationEndDate;

    @Column(name = "booking_id")
    private String bookingId;

    public Reservation() {
    }

    public Reservation(User user, DateTime reservationStartDate, DateTime reservationEndDate) {
        this.user = user;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        bookingId = UUID.randomUUID().toString();
    }

    public DateTime getReservationEndDate() {
        return reservationEndDate;
    }

    public User getUser() {
        return user;
    }

    public DateTime getReservationStartDate() {
        return reservationStartDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReservationStartDate(DateTime reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    public void setReservationEndDate(DateTime reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public class ReservationApiModel {
        private User user;

        private String reservationStartDate;

        private String reservationEndDate;

        private String bookingId;

        public ReservationApiModel() {
        }

        public User getUser() {
            return user;
        }

        public String getReservationStartDate() {
            return reservationStartDate;
        }

        public String getReservationEndDate() {
            return reservationEndDate;
        }

        public String getBookingId() {
            return bookingId;
        }
    }

    public ReservationApiModel toApiModel() {
        ReservationApiModel apiModel = new ReservationApiModel();
        apiModel.user = this.user;
        apiModel.reservationStartDate = this.reservationStartDate.toString();
        apiModel.reservationEndDate = this.reservationEndDate.toString();
        apiModel.bookingId = this.bookingId;
        return apiModel;
    }
}
