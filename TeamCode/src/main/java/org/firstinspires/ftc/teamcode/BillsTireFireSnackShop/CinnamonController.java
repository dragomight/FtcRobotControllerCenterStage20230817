package org.firstinspires.ftc.teamcode.BillsTireFireSnackShop;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfDistance;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

import java.util.ArrayList;

public class CinnamonController { // THE SPICE MUST FLOW
    // Gear Ratio Ratio = 19.2:1
    // Encoder Shaft: 28 pulses per revolution
    // Gearbox output: 537.7 pulses per revolution

    // motor write positions, given in ticks
    public int frontLeftTicks = 0;
    public int frontRightTicks = 0;
    public int backRightTicks = 0;
    public int backLeftTicks = 0;

    public double frontLeft;
    public double frontRight;
    public double backRight;
    public double backLeft;

    public final static int BASE_TICKS = 100; // an arbitrary value
    public final static double TOP_ANGULAR_VELOCITY_DEG = 360 * 3; // deg/sec

    private int sequenceIndex = 0;
    private boolean arrival = true;

    Telemetry telemetry;
    GlyphidGuts glyphidGuts;

    public void initialize(GlyphidGuts glyphidGuts) {
        this.glyphidGuts = glyphidGuts;
        this.telemetry = glyphidGuts.telemetry;
    }

    // manual two-stick control of mechanum drivetrain
    public void update(boolean verbose){

        double xL = limit(glyphidGuts.gamePadState1.leftStickX);
        double yL = limit(glyphidGuts.gamePadState1.leftStickY);
        double xR = limit(glyphidGuts.gamePadState1.rightStickX);
        double yR = limit(glyphidGuts.gamePadState1.rightStickY); // not using this dimension of the stick

        // get the magnitude of the stick deflection
        double magnitude = Math.sqrt(xL*xL + yL*yL + xR*xR);

        // if the magnitude is high, do it full speed
        if (magnitude > .9) {
            frontLeft = -(-yL + xL + xR);//magnitude;
            frontRight = (-yL - xL - xR);//magnitude;
            backRight = (-yL + xL - xR);//magnitude;
            backLeft = -(-yL - xL + xR);//magnitude;
        }
        // if the magnitude is mid-level, do it half speed
        else if(magnitude > .001) {
            frontLeft = -(-yL + xL + xR)/2;//magnitude;
            frontRight = (-yL - xL - xR)/2;//magnitude;
            backRight = (-yL + xL - xR)/2;//magnitude;
            backLeft = -(-yL - xL + xR)/2;//magnitude;
        }
        // if the magnitude is small, just do nothing
        else{
            frontLeft = 0.0;
            frontRight = 0.0;
            backRight = 0.0;
            backLeft = 0.0;
        }

        // insure the numbers are limited in range
        frontLeft = UtilityKit.limitToRange(frontLeft, -1.0, 1.0);
        frontRight = UtilityKit.limitToRange(frontRight, -1.0, 1.0);
        backRight = UtilityKit.limitToRange(backRight, -1.0, 1.0);
        backLeft = UtilityKit.limitToRange(backLeft, -1.0, 1.0);

        glyphidGuts.cinnamonCar.frontRightPower = frontRight;
        glyphidGuts.cinnamonCar.backRightPower = backRight;
        glyphidGuts.cinnamonCar.backLeftPower = backLeft;
        glyphidGuts.cinnamonCar.frontLeftPower =frontLeft;

        if (verbose) {
            telemetry.addData("frontRightPower: ", frontLeft);
            telemetry.addData("backRightPower: ", backRight);
            telemetry.addData("backLeftPower: ", backLeft);
            telemetry.addData("frontLeftPower: ", frontLeft);
        }
    }

    // limits the range of the value to +1 to -1 and squares it, preserving the sign
    private double limit(double value){
        if(value > 1.0){
            value = 1.0;
        }
        else if(value < -1.0){
            value = -1.0;
        }
        return value * Math.abs(value); // squaring the value, but keeping the sign
    }
}
