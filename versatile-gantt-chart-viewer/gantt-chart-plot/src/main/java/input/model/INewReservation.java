package input.model;

import model.Material;

import java.time.LocalDate;

public interface INewReservation {

    String NO_SHARING = "NOSHARING";

    String toString();

    /**
     * @return the plant
     */
    String getPlant();

    /**
     * @return the orderItemText
     */
    String getOrderItemText();

    /**
     * @return the startDate
     */
    LocalDate getStartDate();

    /**
     * @return the finishDate
     */
    LocalDate getFinishDate();

    /**
     * @return the functionalLocation
     */
    String getFunctionalLocation();

    /**
     * @return the quantity
     */
    String getQuantity();

    /**
     * @return the materialType
     */
    String getMaterialType();

    /**
     * @return the storageLocation
     */
    String getStorageLocation();

    /**
     * @return the userName
     */
    String getUserName();

    /**
     * @return the startReservationNumber
     */
    String getStartReservationNumber();

    /**
     * @return the endReservationNumber
     */
    String getEndReservationNumber();

    /**
     * @return the eventType
     */
    String getEventType();

    /**
     * @return the eventId
     */
    String getEventId();

    /**
     * @return the equipment
     */
    String getEquipment();

    /**
     * @return the phase
     */
    String getPhase();

    /**
     * @return the eventStatus
     */
    String getEventStatus();

    /**
     * @return the supplyChainFlow
     */
    String getSupplyChainFlow();

    /**
     * @return the stockStatus
     */
    String getStockStatus();

    /**
     * @return the sharingId
     */
    String getSharingId();

    /**
     * @return the material
     */
    Material getMaterial();

    boolean isLeadingEvent();

    String getHeaderId();

    String getItemId();

    long getUniquePlanningId();

    String getMachineId();
}
