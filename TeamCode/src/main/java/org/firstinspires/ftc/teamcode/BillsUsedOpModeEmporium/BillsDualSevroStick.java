package org.firstinspires.ftc.teamcode.BillsUsedOpModeEmporium;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DipServo", group="Bill's Emporium")
public class BillsDualSevroStick extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private CRServo stick1 = null;
    private CRServo stick2 = null;

    @Override
    public void runOpMode() {
        double stickTarget = 0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //
        stick1 = hardwareMap.get(CRServo.class, "DIP_1");
        stick2 = hardwareMap.get(CRServo.class, "DIP_2");

        stick2.setDirection(CRServo.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //
            stickTarget = -gamepad1.left_stick_y;
            stick1.setPower(stickTarget);
            stick2.setPower(stickTarget);

            // Show Telemetry
            telemetry.addData("Don't worry about that drip~~ ", "Also we have a no refunds policy.");
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Target: ", stickTarget);

            telemetry.update();
        }
    }
}
