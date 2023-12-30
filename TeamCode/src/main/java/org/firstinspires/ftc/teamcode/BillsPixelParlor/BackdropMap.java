package org.firstinspires.ftc.teamcode.BillsPixelParlor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector3D;

public class BackdropMap {
    public GridSlot[][] grid = new GridSlot[13][7];

    Telemetry telemetry;
    GamePadState gamePadState;

    public BackdropMap() {
    }

    public void initialize(GamePadState gamePadState, Telemetry telemetry) {
        this.gamePadState = gamePadState;
        this.telemetry = telemetry;
    }

    public void update() {


    }

    public Vector3D gridPosition(int row, int colum) { // Returns the calculated xyz coordinate of the grid index
        return new Vector3D();
    }
}
