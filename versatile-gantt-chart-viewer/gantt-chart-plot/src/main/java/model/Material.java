package model;

import input.model.INewReservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Material implements Comparable {

    private static Map<String, Material> allMaterialsByCode = new HashMap<>();
    private String materialCode;
    private String materialDescription;
    private List<INewReservation> reservations;

    public Material(String materialCode, String materialDescription) {
        super();
        this.materialCode = materialCode;
        this.materialDescription = materialDescription;
        allMaterialsByCode.put(materialCode, this);
    }

    /**
     * @return the allMaterialsByCode
     */
    public static Material getMaterialByCode(String materialCode) {
        return allMaterialsByCode.get(materialCode);
    }

    /**
     * @return the materialCode
     */
    public String getMaterialCode() {
        return materialCode;
    }

    /**
     * @return the materialDescription
     */
    public String getMaterialDescription() {
        return materialDescription;
    }

    @Override
    public String toString() {
        return this.materialCode + " " + this.materialDescription;
    }

    @Override
    public int compareTo(Object o) {
        return this.materialCode.compareTo(((Material) o).getMaterialCode());
    }

    /**
     * @return the reservations
     */
    public List<INewReservation> getReservations() {
        return reservations;
    }

    public void addReservation(INewReservation r) {
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        reservations.add(r);
    }
}
