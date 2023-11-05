package org.firstinspires.ftc.teamcode.BillsUsedOpModeEmporium;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

@TeleOp(name="Steve!", group="Bill's Emporium")
public class Steve extends LinearOpMode {

    // Create general variables
    private ElapsedTime runtime = new ElapsedTime();
    private GlyphidGuts glyphidGuts = new GlyphidGuts();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Starting...");

        telemetry.update();

        glyphidGuts.initialize(hardwareMap, gamepad1, gamepad2, telemetry, RoboMode.MANUAL);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        telemetry.addData("Status", "Running");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            glyphidGuts.update();
        }}
}
