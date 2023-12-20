package org.firstinspires.ftc.teamcode.NicNacCADilac;

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
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

import java.util.List;

public class CADilacActuators { // I assure you, these actuators were not stolen!
    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    // Declare cinnamon dc motors
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backRight;
    public DcMotorEx backLeft;

    // Declare joint controllers for balustrade
    public DcMotorEx joint1;
    public DcMotorEx joint2;

    // Declare balustrade servos
    public Servo launchMan;

    List<LynxModule> allHubs;
    ElapsedTime timer;
    int cycles;
    CADilac cadilac;

    public CADilacActuators() {

    }

    public void initialize(CADilac cadilac) {
        this.cadilac = cadilac;
        telemetry = cadilac.telemetry;
        hardwareMap = cadilac.hardwareMap;

        try {
            // Important Step 1:  Make sure you use DcMotorEx when you instantiate your motors.
            // Initialize drivetrain dc motors
            frontLeft = hardwareMap.get(DcMotorEx.class, "FrontLeft");
            frontRight = hardwareMap.get(DcMotorEx.class, "FrontRight");
            backRight = hardwareMap.get(DcMotorEx.class, "BackRight");
            backLeft = hardwareMap.get(DcMotorEx.class, "BackLeft");

            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);
            frontRight.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.FORWARD);

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch (Exception e) {
            Log.e("Actuators", "Drivetrain dc motors failed to initialize");
            Log.e("Actuators", e.toString());
        }

        try {
            // Initialize arm dc motors
            joint1 = hardwareMap.get(DcMotorEx.class, "Joint1");
            joint2 = hardwareMap.get(DcMotorEx.class, "Joint2");

            joint1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            joint2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            joint2.setDirection(DcMotorSimple.Direction.REVERSE);

            joint1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            joint2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            launchMan = hardwareMap.get(Servo.class, "Launcher");
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

        //UPDOOT
        cadilac.cinnamonController.update(verbose);
        cadilac.yarmController.update(verbose);

        // SET THE DC MOTORS AND SERVOS
        set();
    }

    private void read() {
        cadilac.yarm.joint1Ticks = joint1.getCurrentPosition();
        cadilac.yarm.joint2Ticks = joint2.getCurrentPosition();

        cadilac.cinnamonCar.frontRightTicks = frontRight.getCurrentPosition();
        cadilac.cinnamonCar.backRightTicks = frontRight.getCurrentPosition();
        cadilac.cinnamonCar.backLeftTicks = backLeft.getCurrentPosition();
        cadilac.cinnamonCar.frontLeftTicks = frontLeft.getCurrentPosition();
    }

    private void set() {
        if (cadilac.yarm.launch) {
            launchMan.setPosition(0.5);
        }
        else {
            launchMan.setPosition(0);
        }

        //joint1.setVelocity(cadilac.yarm.joint1Velocity);
        //joint2.setVelocity(cadilac.yarm.joint2Velocity);\

        joint1.setTargetPosition(cadilac.yarm.joint1Target);
        joint2.setTargetPosition(cadilac.yarm.joint2Target);

        joint1.setPower(1);
        joint2.setPower(1);

        joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        joint2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Send calculated power to wheels
        //frontRight.setPower(cadilac.cinnamonCar.frontRightPower);
        //backRight.setPower(cadilac.cinnamonCar.backRightPower);
        //backLeft.setPower(cadilac.cinnamonCar.backLeftPower);
        //frontLeft.setPower(cadilac.cinnamonCar.frontLeftPower);

        frontRight.setVelocity(cadilac.cinnamonCar.frontRightVelocity);
        backRight.setVelocity(cadilac.cinnamonCar.backRightVelocity);
        backLeft.setVelocity(cadilac.cinnamonCar.backLeftVelocity);
        frontLeft.setVelocity(cadilac.cinnamonCar.frontLeftVelocity);
    }
}
