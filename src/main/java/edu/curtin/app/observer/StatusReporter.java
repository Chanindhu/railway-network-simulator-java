package edu.curtin.app.observer;

/**
 * An interface that extends SimulationObserver to define 
 * a specialized observer  for reporting status updates in the railway network simulation.
 * 
 * for observers that only need to report status, potentially filtering or formatting events
 * received from the simulation lifecycle.
 * 
 * 
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file StatusReporter.java
 * @since 2025-05-27
 */
public interface StatusReporter extends SimulationObserver {
}
