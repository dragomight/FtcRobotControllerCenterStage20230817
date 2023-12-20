package org.firstinspires.ftc.teamcode.BillsTireFireSnackShop;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.DCMotorWriteMode;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfDistance;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

import java.util.ArrayList;

public class CinnamonController { // THE SPICE MUST FLOW
    private final double MAX_TICKS_PER_SECOND = (312.0/60)*530;

    // Gear Ratio Ratio = 19.2:1
    // Encoder Shaft: 28 pulses per revolution
    // Gearbox output: 537.7 pulses per revolution

    Telemetry telemetry;
    GamePadState gamePadState;
    CinnamonCar cinnamonCar;
    DCMotorWriteMode driveMode;

    public void initialize(GamePadState gamePadState, CinnamonCar cinnamonCar, Telemetry telemetry, DCMotorWriteMode driveMode) {
        this.telemetry = telemetry;
        this.gamePadState = gamePadState;
        this.cinnamonCar = cinnamonCar;
        this.driveMode = driveMode;
    }

    // manual two-stick control of mechanum drivetrain
    public void update(boolean verbose){
        double max;

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial   = -gamePadState.leftStickY;  // Note: pushing stick forward gives negative value
        double lateral =  gamePadState.leftStickX;
        double yaw     =  gamePadState.rightStickX;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower  = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower   = axial - lateral + yaw;
        double rightBackPower  = axial + lateral - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;
        }

        if (driveMode == DCMotorWriteMode.POWER) {
            cinnamonCar.frontRightPower = rightFrontPower;
            cinnamonCar.backRightPower = rightBackPower;
            cinnamonCar.backLeftPower = leftBackPower;
            cinnamonCar.frontLeftPower = leftFrontPower;
        }

        else if (driveMode == DCMotorWriteMode.RUN_FOR_SPEED) {
            cinnamonCar.frontRightVelocity = rightFrontPower*MAX_TICKS_PER_SECOND;
            cinnamonCar.backRightVelocity = rightBackPower*MAX_TICKS_PER_SECOND;
            cinnamonCar.backLeftVelocity = leftBackPower*MAX_TICKS_PER_SECOND;
            cinnamonCar.frontLeftVelocity = leftFrontPower*MAX_TICKS_PER_SECOND;
        }

        if (verbose) {
            telemetry.addData("frontRightPower: ", rightFrontPower);
            telemetry.addData("backRightPower: ", rightBackPower);
            telemetry.addData("backLeftPower: ", leftBackPower);
            telemetry.addData("frontLeftPower: ", leftFrontPower);
        }
    }
/*
    public DriveMove moveInDirection(double distance, UnitOfDistance unitOfDistance, double angle, UnitOfAngle unitOfAngle, String moveName) {
        if (unitOfDistance != UnitOfDistance.IN) {
            return null;
        }

        if (unitOfAngle != UnitOfAngle.DEGREES) {
            return null;
        }

        Vector2D target = new Vector2D(UtilityKit.sin(angle, UnitOfAngle.DEGREES) * distance, UtilityKit.cos(angle, UnitOfAngle.DEGREES) * distance);

        double frontLeft = target.getY() + target.getX();
        double frontRight = target.getY() - target.getX();
        double backRight = target.getY() + target.getX();
        double backLeft = target.getY() - target.getX();

        int frontLeftTicks = driveDistanceToTicks(frontLeft);
        int frontRightTicks = driveDistanceToTicks(frontRight);
        int backRightTicks = driveDistanceToTicks(backRight);
        int backLeftTicks = driveDistanceToTicks(backLeft);

        return new DriveMove(frontLeftTicks, frontRightTicks, backRightTicks, backLeftTicks, moveName);
    }

    private int driveDistanceToTicks(double distance) { //INCHES
        return (int)(1.0);
    } // TODO

 */
}
