package org.firstinspires.ftc.teamcode.BillsYarm;

public class Yoint {
    // Motor numbers
    private static final double MAX_DEGREE_PER_SECOND = 180;
    private static final double TICK_PER_REV = 5281.1;
    private static final double TICK_PER_DEGREE = TICK_PER_REV/360;
    private static final double DEGREE_PER_TICK = 360/TICK_PER_REV;

    // Numbers set using data from actuators class
    private int ticks;
    private double ticksPerSecond;
    private double angle;
    private double velocity;

    // Limits of joint
    public double maxAngle;
    public double minAngle;
    public double homeAngle;

    public Yoint(double maxAngle, double minAngle, double homeAngle) {
        this.maxAngle = maxAngle;
        this.minAngle = minAngle;
        this.homeAngle = homeAngle;
    }

    public void updateTicks(int ticks) {
        this.ticks = ticks;
        angle = ticksToDegree(ticks)+homeAngle;
    }

    public void updateTicksPerSecond(double ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        velocity = ticksToDegree(ticksPerSecond);
    }

    public double getAngle() {return angle;}

    public double getVelocity() {return velocity;}

    public static double ticksToDegree(double ticks) {
        return ticks*DEGREE_PER_TICK;
    }

    public static int degreesToTicks(double degrees) {
        return (int)(degrees*TICK_PER_DEGREE);
    }
}
