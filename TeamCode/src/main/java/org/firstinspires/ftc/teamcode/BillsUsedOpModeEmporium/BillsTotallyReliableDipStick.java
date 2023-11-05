package org.firstinspires.ftc.teamcode.BillsUsedOpModeEmporium;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DipStick", group="Bill's Emporium")
//@Disabled
public class BillsTotallyReliableDipStick extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx stick = null;

    @Override
    public void runOpMode() {
        int stickTicks = 0;
        int stickTarget = 0;
        int pullStrength = 10;
        double pull = 0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //
        stick = hardwareMap.get(DcMotorEx.class, "DIP_ME");

        //
        stick.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //
            stickTicks = stick.getCurrentPosition();

            //
            pull = -gamepad1.left_stick_y;

            //
            if (gamepad1.dpad_up) {
                pullStrength++;
            }
            else if (gamepad1.dpad_down) {
                pullStrength--;
            }

            //
            stickTarget = (int)pull*pullStrength;

            //
            stick.setVelocity(stickTarget);

            //
            if (stick.isBusy()) {
                stick.setPower(1);
            }
            else {
                stick.setPower(0);
            }

            stick.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Show Telemetry
            telemetry.addData("Don't worry about that drip~~ ", "Also we have a no refunds policy.");
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Strength: ", pullStrength);
            telemetry.addData("Target: ", stickTarget);
            telemetry.addData("Position: ", stickTicks);
            telemetry.update();
        }
    }
}
