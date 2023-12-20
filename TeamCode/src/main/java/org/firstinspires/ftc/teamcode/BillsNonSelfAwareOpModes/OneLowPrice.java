package org.firstinspires.ftc.teamcode.BillsNonSelfAwareOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "For One Low Price", group = "Auto")
public class OneLowPrice extends LinearOpMode {
    // Create general variables
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() throws InterruptedException {
        DcMotorEx frontRight = (DcMotorEx) this.hardwareMap.dcMotor.get("FrontRight");
        DcMotorEx backRight = (DcMotorEx) this.hardwareMap.dcMotor.get("BackRight");
        DcMotorEx backLeft = (DcMotorEx) this.hardwareMap.dcMotor.get("BackLeft");
        DcMotorEx frontLeft = (DcMotorEx) this.hardwareMap.dcMotor.get("FrontLeft");

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Waiting for Play", "Wait for Referees and then Press Play");
        telemetry.update();
        waitForStart();

        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Scraping the bottom of the barrel ", true);

            if (runtime.milliseconds() < 1800) {
                frontRight.setPower(.5);
                backRight.setPower(.5);
                backLeft.setPower(.5);
                frontLeft.setPower(.5);
            }

            else if (runtime.milliseconds() > 1800 && runtime.milliseconds() < 2000) {
                frontRight.setPower(-.5);
                backRight.setPower(-.5);
                backLeft.setPower(-.5);
                frontLeft.setPower(-.5);
            }

            //else if (runtime.milliseconds() < 3000) {
                //frontRight.setPower(-.25);
                //backRight.setPower(-.25);
                //backLeft.setPower(-.25);
                //frontLeft.setPower(-.25);
            //}

            else {
                frontRight.setPower(0);
                backRight.setPower(0);
                backLeft.setPower(0);
                frontLeft.setPower(0);
            }

            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            telemetry.update();
        }
    }
}
