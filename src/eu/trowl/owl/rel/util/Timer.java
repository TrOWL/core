package eu.trowl.owl.rel.util;


/**
 * File name: Timer.java
 * <p><b>Description</b>: This class is created for timing the different part of the software.</p>
 *
 * @author Quentin Reul (q.reul@abdn.ac.uk)
 * @version 1.0
 * Created on 11 Feb 2008
 */
public class Timer {
	private boolean started;				// Check whether the Timer is already started
	private String name;					// Name of the task timed
	private long startTime;					// Time at which the Timer was started 
	private long lastTime;					// Time @ which the Timer was stopped
	private long totalTime;					// total time that has elapsed when the timer was running
	private int count;						// Number of time it was used
	/** 
	 * Constructor for Timer
	 * @param n being the name of the task to be timed
	 */
	public Timer(String n) {
		this.name = n;
		reset();
	}
	/**
	 * Reset all the counters associated with this timer.
	 */
	public void reset() {
		this.started = false;
		this.totalTime = 0;
		this.startTime = 0;
		this.lastTime = 0;
		this.count = 0;
	}
	/**
	 * Start time timer by recording the time this function is called. If the timer is already 
	 * started, then inform the user.
	 */
	public void start() {
		if ( !this.started ) {
			this.startTime = System.currentTimeMillis();
			this.started = true;
		}
		this.count++;
	}
	/**
	 * Stop the timer, increment the count and update the total time spent. If the timer has not 
	 * been stater yet, then inform the user.
	 */
	public void stop() {
		if ( this.started ) {
			this.lastTime = System.currentTimeMillis() - this.startTime;
			this.totalTime += lastTime;
			this.started = false;
		}
	}
	/**
	 * Return the name of this timer.
	 * @return the name of the task timed
	 */
	public String getName() {
	    return name;
	}
	/**
	 * Function to get the total time during which the task ran.
	 * @return the total time (in milliseconds) spent while this timer was running.
	 */
	public double getTotal() {
		return totalTime / 1000.0;
	}
	/**
	 * Function to calculate the average time taken to the task
	 * @return the Average time taken for accomplish the task.
	 */
	public double getAverage() {
		return (double) totalTime / count;
	}
	/**
	 * Return the total time spent between last start()-stop() period.
	 * @return the last time
	 */
	public long getLast() {
		return lastTime;
	}
	/**
	 * Overriding the normal Object.toString() method
     * @return a String representation of the object
	 */
	public String toString() {		
		if ( this.count > 1 ) {
			return name + " Avg: " + getAverage() + " Count: " + count + " Total: " + getTotal() + " s";
		} else {
			return name + " Total: " + getTotal() + " s";
		}		
	}
}
