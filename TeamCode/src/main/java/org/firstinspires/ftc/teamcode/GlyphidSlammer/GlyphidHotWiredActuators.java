package org.firstinspires.ftc.teamcode.GlyphidSlammer;

import android.util.Log;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.DCMotorWriteMode;
import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.BillsTireFireSnackShop.CinnamonController;

import java.util.List;

public class GlyphidHotWiredActuators { // I assure you, these actuators were not stolen!
    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    // Declare cinnamon dc motors
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backRight;
    public DcMotorEx backLeft;

    // Declare joint controllers for balustrade
    public DcMotorEx joint;
    public DcMotorEx extendo;

    // Declare balustrade servos
    public Servo sirLatch;
    public Servo launchMan;
    public Servo ctrlR;
    public Servo barrelRoll;
    public Servo gripA;
    public Servo gripB;

    List<LynxModule> allHubs;
    ElapsedTime timer;
    int cycles;
    GlyphidGuts glyphidGuts;

    public GlyphidHotWiredActuators() {

    }

    public void initialize(GlyphidGuts glyphidGuts) {
        this.glyphidGuts = glyphidGuts;
        telemetry = glyphidGuts.telemetry;
        hardwareMap = glyphidGuts.hardwareMap;

        try {
            // Important Step 1:  Make sure you use DcMotorEx when you instantiate your motors.
            // Initialize drivetrain dc motors
            frontLeft = hardwareMap.get(DcMotorEx.class, "FrontLeft");
            frontRight = hardwareMap.get(DcMotorEx.class, "FrontRight");
            backRight = hardwareMap.get(DcMotorEx.class, "BackRight");
            backLeft = hardwareMap.get(DcMotorEx.class, "BackLeft");

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e) {
            Log.e("Actuators", "Drivetrain dc motors failed to initialize");
            Log.e("Actuators", e.toString());
        }

        try {
            // Initialize arm dc motors
            joint = hardwareMap.get(DcMotorEx.class, "Joint");
            extendo = hardwareMap.get(DcMotorEx.class, "Extendo");

            joint.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            joint.setDirection(DcMotorSimple.Direction.REVERSE);

            joint.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            // Important Step 2: Get access to a list of Expansion Hub Modules to enable changing caching methods.
            allHubs = hardwareMap.getAll(LynxModule.class);

            timer = new ElapsedTime();

            // Important Step 3: Option B. Set all Expansion hubs to use the MANUAL Bulk Caching mode
            for (LynxModule module : allHubs) {
                module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            }
        }
        catch (Exception e) {
            Log.e("Actuators: initialize", "Arm dc motors failed to initialize");
            Log.e("Actuators: initialize", e.toString());
        }

        try {
            // Initialize arm servo motors
            //sirLatch = hardwareMap.get(Servo.class, "Latch");
            launchMan = hardwareMap.get(Servo.class, "Launcher");
            //ctrlR = hardwareMap.get(Servo.class, "pitch");
            //barrelRoll = hardwareMap.get(Servo.class, "roll");
            gripA = hardwareMap.get(Servo.class, "GripA");
            //gripB = hardwareMap.get(Servo.class, "GripB");
        }
        catch (Exception e) {
            System.out.println("Servos failed to initialize");
            Log.e("Actuators", "Servo motors failed to initialize");
            Log.e("Actuators", e.toString());
        }
    }

    public void update(boolean verbose) {
        timer.reset();
        cycles = 0;
        // Important Step 4: If you are using MANUAL mode, you must clear the BulkCache once per control cycle
        for (LynxModule module : allHubs) {
            module.clearBulkCache();
        }

        // READ ALL DC MOTORS
        read();

        glyphidGuts.cinnamonController.update(verbose);
        glyphidGuts.balustradeController.update(verbose);

        // SET THE DC MOTORS AND SERVOS
        set();
    }

    private void read() {
        glyphidGuts.leBalustrade.joint.setAngleByTicks(joint.getCurrentPosition());
        glyphidGuts.leBalustrade.extendo.setLengthByTicks(extendo.getCurrentPosition());

        // Read drivetrain motors
        //TODO: Read
    }

    private void set() {
        if (glyphidGuts.leBalustrade.dcMotorWriteMode == DCMotorWriteMode.RUN_FOR_SPEED) {
            joint.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else if (glyphidGuts.leBalustrade.dcMotorWriteMode == DCMotorWriteMode.RUN_TO_POSITION) {
            joint.setTargetPosition((int) glyphidGuts.leBalustrade.joint.getTargetTicks());
            //extendo.setTargetPosition((int) glyphidGuts.leBalustrade.extendo.motor.getTargetTicks());

            telemetry.addData("joint: ", glyphidGuts.leBalustrade.joint.getTargetTicks());
            telemetry.addData("Extendo: ", glyphidGuts.leBalustrade.extendo.motor.getTargetTicks());

            /*
            if (joint.isBusy()) {
                joint.setPower(1);
            }
            else {
                joint.setPower(0);
            }
            if (extendo.isBusy()) {
                extendo.setPower(1);
            }
            else {
                extendo.setPower(0);
            }
             */

            joint.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //extendo.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            joint.setPower(1);
            //extendo.setPower(1);
        }

        // Cinnamon
        frontRight.setPower(glyphidGuts.cinnamonCar.frontRightPower);
        backRight.setPower(glyphidGuts.cinnamonCar.backRightPower);
        backLeft.setPower(glyphidGuts.cinnamonCar.backLeftPower);
        frontLeft.setPower(glyphidGuts.cinnamonCar.frontLeftPower);

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Les servomoteurs du Balustrade
        if (glyphidGuts.leBalustrade.engageSirLatch) {
            //sirLatch.setPosition(0);
        }
        else {
            //sirLatch.setPosition(1);
        }
        if (glyphidGuts.leBalustrade.grip) {
            gripA.setPosition(0);
            //gripA.setPosition(1);
            //gripB.setPosition(1);
        }
        else {
            gripA.setPosition(120/300.0);
            //gripA.setPosition(0);
            //gripB.setPosition(0);
        }
        if (glyphidGuts.leBalustrade.launch) {
            launchMan.setPosition(0);
        }
        else {
            launchMan.setPosition(1);
        }
        //ctrlR.setPosition(glyphidGuts.leBalustrade.rAngle/300.0         + 0.5);
        //barrelRoll.setPosition(glyphidGuts.leBalustrade.rollAngle/300.0 + 0.5);
    }
}
