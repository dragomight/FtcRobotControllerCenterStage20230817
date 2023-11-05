package org.firstinspires.ftc.teamcode.GlyphidSlammer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class GlyphidGMOSensors {
    Telemetry telemetry;
    HardwareMap hardwareMap;

    // Time (time passed since the last data snapshot is useful for physics calculations)
    private ElapsedTime runtime = new ElapsedTime();

    // Limit sensors
    private TouchSensor extendoSensor;
    private DigitalChannel balustradeSensor;

    private double time = 0;
    private double lastTime = 0;
    private double dt = 0; // the time interval since the last update function cal

    GlyphidGuts glyphidGuts;

    public GlyphidGMOSensors() {

    }

    public void initialize(GlyphidGuts glyphidGuts) {
        this.glyphidGuts = glyphidGuts;
        telemetry = glyphidGuts.telemetry;
        hardwareMap = glyphidGuts.hardwareMap;

        try {
            //TODO: Determine whether IMU is required
            //extendoSensor = hardwareMap.get(TouchSensor.class, "ExtenderLimit");
            //balustradeSensor = hardwareMap.get(DigitalChannel.class, "JointLimit");

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

            // Read the limiter switches buttons and magnet
            //glyphidGuts.leBalustrade.extendoLimit = extendoSensor.isPressed();
            //glyphidGuts.leBalustrade.balustradeLimit = balustradeSensor.getState();

            if(verbose) {
                // buttons
                telemetry.addData("Extender limit: ", glyphidGuts.leBalustrade.extendoLimit);
                telemetry.addData("Joint limit: ", glyphidGuts.leBalustrade.balustradeLimit);
            }
        }

        catch (Exception e) {
            telemetry.addData("Sensor update failure ", e.toString());
        }
    }
}
