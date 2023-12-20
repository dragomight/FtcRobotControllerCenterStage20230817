package org.firstinspires.ftc.teamcode.NicNacCADilac;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

public class CADilacSensors {
    Telemetry telemetry;
    HardwareMap hardwareMap;

    // Time (time passed since the last data snapshot is useful for physics calculations)
    private ElapsedTime runtime = new ElapsedTime();

    IMU imu;
    YawPitchRollAngles orientation;
    AngularVelocity angularVelocity;

    private double time = 0;
    private double lastTime = 0;
    private double dt = 0; // the time interval since the last update function cal

    CADilac cadilac;

    public CADilacSensors() {

    }

    public void initialize(CADilac cadilac) {
        this.cadilac = cadilac;
        telemetry = cadilac.telemetry;
        hardwareMap = cadilac.hardwareMap;

        try {
            imu = hardwareMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

            RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
            imu.resetYaw();

            orientation = imu.getRobotYawPitchRollAngles();

            runtime.reset();
        }
        catch (Exception e){
            telemetry.addData("GMO Failed! ", e.toString());
            telemetry.update();
        }
    }

    public void update(boolean verbose) {
        try {
            time = runtime.seconds();
            dt = time - lastTime;
            lastTime = time;

            // Check to see if heading reset is requested
            if (cadilac.gamepad1.b) {
                telemetry.addData("Yaw", "Resetting\n");
                imu.resetYaw();
            }

            orientation = imu.getRobotYawPitchRollAngles();
            angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            if(verbose) {
                telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
                telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
                telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
                telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
                telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
                telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
            }
        }

        catch (Exception e) {
            telemetry.addData("Sensor update failure ", e.toString());
        }
    }
}
