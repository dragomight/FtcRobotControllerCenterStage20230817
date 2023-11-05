package org.firstinspires.ftc.teamcode.BillsRailwayBakery;

import org.firstinspires.ftc.teamcode.BillsEs.DCMotorWriteMode;
import org.firstinspires.ftc.teamcode.BillsEs.MotorType;

public class LeBalustrade {
    public DCArmJoint joint = new DCArmJoint(-90, 90, 90, MotorType.BIG);
    public DCArmExtender extendo = new DCArmExtender(97, MotorType.DRIVETRAIN);
    public DCMotorWriteMode dcMotorWriteMode = DCMotorWriteMode.RUN_TO_POSITION;

    // Servo control
    public boolean engageSirLatch = false;
    public boolean launch = false;
    public boolean grip = true;
    public double rAngle = -90;
    public double rollAngle = 0;

    // Limit booleans
    public boolean extendoLimit;
    public boolean balustradeLimit;

    public LeBalustrade() {

    }
}
