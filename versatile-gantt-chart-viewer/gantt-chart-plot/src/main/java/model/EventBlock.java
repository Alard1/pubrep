package model;

import input.model.INewReservation;

import java.time.LocalDate;
import java.util.*;

/**
 * @author alsnippe The event block class represents a single square in a gantt chart. it has two
 *     constructors: (1) one with a single reservation, when this square in the gantt chart
 *     represents only 1 reservation, and (2) one with a list of reservations (which before
 *     initialization of the event block have been clustered) when the square represents a multitude
 *     of reservations (e.g. a full event, with event ID)
 */
public class EventBlock extends TaskPlus {

    private final List<INewReservation> reservationList = new ArrayList<>();

    private EventBlock(INewReservation reservation) {

        super(reservation.getStartDate(), reservation.getFinishDate());

        this.reservationList.add(reservation);
    }

    private EventBlock(LocalDate start, LocalDate end, List<INewReservation> reservation) {

        super(start, end);

        this.reservationList.addAll(reservation);
    }

    public static EventBlock createNewEventBlock(INewReservation reservation) {

        return new EventBlock(reservation);
    }

    public static EventBlock createNewEventBlockBasedOnMultipleReservations(
            List<INewReservation> reservationsForSharingIdForGivenMaterial) {

        LocalDate startDate =
                reservationsForSharingIdForGivenMaterial.stream()
                        .map(INewReservation::getStartDate)
                        .min(Comparator.naturalOrder())
                        .get();
        LocalDate endDate =
                reservationsForSharingIdForGivenMaterial.stream()
                        .map(INewReservation::getFinishDate)
                        .max(Comparator.naturalOrder())
                        .get();

        return new EventBlock(startDate, endDate, reservationsForSharingIdForGivenMaterial);
    }

    /**
     * @return the reservation
     */
    public INewReservation getReservation() {
        return this.reservationList.get(0);
    }

    /**
     * @return the reservationList
     */
    public List<INewReservation> getReservationList() {
        return reservationList;
    }


}
