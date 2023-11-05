package org.firstinspires.ftc.teamcode.BillsRailwayBakery;

import org.checkerframework.checker.units.qual.degrees;
import org.firstinspires.ftc.teamcode.BillsEs.MotorType;

public class DCArmExtender {
    public static final double PULLEY_RADIUS = 38.2/20; // CM
    private final double circumference = 2*Math.PI*PULLEY_RADIUS;

    public DCArmJoint motor;
    public double maxLength;
    MotorType motorType;

    public double currentLength = 0;
    public double targetLength = 1;

    public DCArmExtender (double maxLength, MotorType motorType) { // CM
        this.maxLength = maxLength;
        this.motorType = motorType;
        motor = new DCArmJoint(0, lengthToDegrees(maxLength), 1, motorType);
    }

    public void setTargetLength(double targetLength) {
        this.targetLength = targetLength;
        motor.setTargetAngle(lengthToDegrees(targetLength));
    }

    public void incrementTargetLength(double length) {
        this.targetLength += length;
        motor.setTargetAngle(lengthToDegrees(targetLength));
    }

    public void setLengthByTicks(int ticks) {
        currentLength = degreesToLength((360/DCArmJoint.PPR_0019)*ticks);
    }

    public double degreesToLength(double degrees) {
        return (circumference/360)*degrees;
    }

    public double lengthToDegrees(double length) {
        return length/(circumference/360);
    }
}
