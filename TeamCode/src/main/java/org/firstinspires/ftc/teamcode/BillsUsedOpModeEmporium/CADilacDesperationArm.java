package org.firstinspires.ftc.teamcode.BillsUsedOpModeEmporium;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.NicNacCADilac.CADilac;

@TeleOp(name="CADilacArm", group="Bill's Emporium")
public class CADilacDesperationArm extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();

    private CADilac cadilac = new CADilac();

    private Servo launcher;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Starting...");
        telemetry.update();

        cadilac.initialize(hardwareMap, gamepad1, gamepad2, telemetry, RoboMode.MANUAL);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            cadilac.update();

            // Show the elapsed game time and wheel power.
            telemetry.addData("SHOP AT ", "BILL TECH TODAY");
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
