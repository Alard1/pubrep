package input.model;


import java.time.LocalDate;

public interface INewReservation {

    String toString();

    LocalDate getStartDate();

    LocalDate getFinishDate();
}
