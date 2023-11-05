package org.firstinspires.ftc.teamcode.GlyphidSlammer;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.BillsEs.RoboType;
import org.firstinspires.ftc.teamcode.BillsRailwayBakery.BalustradeController;
import org.firstinspires.ftc.teamcode.BillsRailwayBakery.LeBalustrade;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsTireFireSnackShop.CinnamonCar;
import org.firstinspires.ftc.teamcode.BillsTireFireSnackShop.CinnamonController;

public class GlyphidGuts {
    public RoboType type = RoboType.GLYPHID_SLAMMER;
    public RoboMode mode;

    public LeBalustrade leBalustrade;
    public BalustradeController balustradeController;
    public CinnamonCar cinnamonCar;
    public CinnamonController cinnamonController;
    public GlyphidGMOSensors sensors;
    public GlyphidHotWiredActuators actuators;

    public GamePadState gamePadState1;
    public GamePadState gamePadState2;

    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;

    public GlyphidGuts() {
        Log.d("Godrick", "Constructing");
        try {
            leBalustrade = new LeBalustrade();
            balustradeController = new BalustradeController();
            cinnamonCar = new CinnamonCar();
            cinnamonController = new CinnamonController();
            sensors = new GlyphidGMOSensors();
            actuators = new GlyphidHotWiredActuators();
            gamePadState1 = new GamePadState();
            gamePadState2 = new GamePadState();
        }
        catch (Exception e){
            Log.e("Nooo Steeeve!", e.toString());
            return;
        }
        Log.d("Steeeeeeeeeve", "Construction Completed");
    }

    public void initialize(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, RoboMode roboMode) {
        try {
            this.hardwareMap = hardwareMap;
            this.gamepad1 = gamepad1;
            this.gamepad2 = gamepad2;
            this.telemetry = telemetry;
            this.mode = roboMode;

            gamePadState1.initialize(telemetry);
            gamePadState2.initialize(telemetry);
            actuators.initialize(this);
            sensors.initialize(this);
            cinnamonController.initialize(this);
            Log.i("Steve: initialize", "The spice flows");
            balustradeController.initialize(this);
            Log.i("Steve: initialize", "BAGUETTE INITIALIZED");
        }

        catch (Exception e){
            Log.e("Godrick: initialize", "FAILED: " + e.toString());
            return;
        }
        Log.i("Steve", "HE LIVES!");
    }

    public void update() {
        telemetry.addData("Mode: ", mode);

        if (mode == RoboMode.MANUAL) {
            manualUpdate();
        }
        else {
            autonomousUpdate();
        }

        // display all telemetry updates to the controller, use verbose=true to see reports in telemetry
        telemetry.update();
    }

    private void manualUpdate() {
        telemetry.addData("FOR ROCK AND STONE EVERYBODY!", " ROCK AND STONE!");
        telemetry.addData("ROCK   AND ", "STONE");

        // read the latest gamepad state
        gamePadState1.update(gamepad1, false);
        gamePadState2.update(gamepad2, false);
        telemetry.addData("Alt mode:", gamePadState1.altMode);

        // read the sensors
        sensors.update(false);

        // read and update the actuators
        actuators.update(true);
    }

    private void autonomousUpdate() {

    }
}
