package org.firstinspires.ftc.teamcode.BillsNonSelfAwareOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.TensorSurfer;

@Autonomous(name = "BackstageBumbleDum", group = "Auto")
public class BackstageBumbleDum extends LinearOpMode {

    // Create general variables
    private ElapsedTime runtime = new ElapsedTime();
    TensorSurfer surfer;
    SpikeMark target;
    public static final String webcam = "Webcam 1";
    //Godrick godrick = new Godrick();

    public void runOpMode() throws InterruptedException {
        surfer = new TensorSurfer(telemetry, runtime);
        surfer.init(hardwareMap);

        while (opModeInInit()) {
            surfer.scanMarks();

            telemetry.update();
        }

        surfer.disable();
        target = surfer.mark;

        // Run required drive sequence
        while (opModeIsActive()) {
            //godrick.parkingUpdate();

            telemetry.update();
        }
    }
}
