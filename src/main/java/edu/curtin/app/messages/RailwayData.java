package edu.curtin.app.messages;

/**
 * represents data for a  railway link in the railway network simulation
 * store the names of two  towns connected by the railway
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayData.java
 * @since 2025-05-27
 */
public class RailwayData {
    private final String town1;
    private final String town2;

    /**
     * constructs a RailwayData object wiht  the names of two towns
     *
     * @param town1  the name of the first town
     * @param town2 the name of the second town
     */
    public RailwayData(String town1, String town2) {
        this.town1 = town1;
        this.town2 = town2;
    }

    /**
     * retrieves the name of the first town
     *
     * @return the name of the first town
     */
    public String getTown1() {
        return town1;
    }

    /**
     * retrieves  thee name of the second town
     *
     * @return the name of the second town
     */
    public String getTown2() {
        return town2;
    }
}