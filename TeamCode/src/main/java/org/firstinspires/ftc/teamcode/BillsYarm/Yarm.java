package org.firstinspires.ftc.teamcode.BillsYarm;

public class Yarm {
    private static final double TICK_PER_REV = 5281.1;
    private static final double TICK_PER_DEGREE = TICK_PER_REV/360;
    private static final double DEGREE_PER_TICK = 360/TICK_PER_REV;

    public int joint1Ticks;
    public int joint2Ticks;

    public double joint1Angle;
    public double joint2Angle;

    public int joint1Target;
    public int joint2Target;

    public int joint1Velocity;
    public int joint2Velocity;

    public boolean launch = false;

    public Yarm() {
    }

    public void updateAngles() {
        joint1Angle = ticksToDegree(joint1Ticks);
        joint2Angle = ticksToDegree(joint2Ticks);
    }

    public static double ticksToDegree(int ticks) {
        return ticks*DEGREE_PER_TICK;
    }

    public static int degreesToTicks(double degrees) {
        return (int)(degrees*TICK_PER_DEGREE);
    }
}
