package org.firstinspires.ftc.teamcode.NicNacCADilac;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.DCMotorWriteMode;
import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsTireFireSnackShop.CinnamonCar;
import org.firstinspires.ftc.teamcode.BillsTireFireSnackShop.CinnamonController;
import org.firstinspires.ftc.teamcode.BillsYarm.Yarm;
import org.firstinspires.ftc.teamcode.BillsYarm.YarmController;

public class CADilac {
    public RoboMode mode;

    public GamePadState gamePadState1;
    public GamePadState gamePadState2;

    public CADilacActuators actuators;
    public CADilacSensors sensors;

    public Yarm yarm;
    public YarmController yarmController;

    public CinnamonCar cinnamonCar;
    public CinnamonController cinnamonController;

    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;

    public CADilac() {
        Log.d("CADilac", "Constructing");
        try {
            actuators = new CADilacActuators();
            sensors = new CADilacSensors();

            yarm = new Yarm();
            yarmController = new YarmController();

            cinnamonCar = new CinnamonCar();
            cinnamonController = new CinnamonController();

            gamePadState1 = new GamePadState();
            gamePadState2 = new GamePadState();
        }
        catch (Exception e){
            Log.e("THE OIL TANK IS LEAKING!", e.toString());
            return;
        }
        Log.d("CADilac", "Construction Completed");
    }

    public void initialize(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, RoboMode roboMode) {
        try {
            this.hardwareMap = hardwareMap;
            this.gamepad1 = gamepad1;
            this.gamepad2 = gamepad2;
            this.telemetry = telemetry;
            this.mode = roboMode;

            actuators.initialize(this);
            sensors.initialize(this);

            yarmController.initialize(gamePadState1, yarm, telemetry);
            cinnamonController.initialize(gamePadState1, cinnamonCar, telemetry, DCMotorWriteMode.RUN_FOR_SPEED);

            gamePadState1.initialize(telemetry);
            gamePadState2.initialize(telemetry);
            Log.i("CADilac: initialize", "Tank filled");
        }

        catch (Exception e){
            Log.e("CADilac: initialize", "FAILED: " + e.toString());
            return;
        }
        Log.i("CADilac", "Vrrrrrrr");
    }

    public void update() {
        telemetry.addData("Mode: ", mode);

        if (mode == RoboMode.MANUAL) {
            manualUpdate();
        }
        else {
            autonomousUpdate();
        }
    }

    private void manualUpdate() {
        telemetry.addData("Don't worry or panic!", " It's only used like new!");

        // read the latest gamepad state
        gamePadState1.update(gamepad1, false);
        gamePadState2.update(gamepad2, false);
        telemetry.addData("Alt mode:", gamePadState1.altMode);

        sensors.update(true);

        // read and update the actuators
        actuators.update(false);
    }

    private void autonomousUpdate() {

    }
}
