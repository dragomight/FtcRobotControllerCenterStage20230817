package org.firstinspires.ftc.teamcode.BillsYarm;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;

import java.util.ArrayList;

public class YarmController {
    private final int VELOCITY = Yarm.degreesToTicks(45.0/2);

    private ArrayList<Integer> joint1Record;
    private ArrayList<Integer> joint2Record;

    private ElapsedTime checkTime;

    Telemetry telemetry;
    GamePadState gamePadState;
    Yarm yarm;

    public void initialize(GamePadState gamePadState, Yarm yarm, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.gamePadState = gamePadState;
        this.yarm = yarm;

        joint1Record = new ArrayList<>();
        joint2Record = new ArrayList<>();

        checkTime = new ElapsedTime();
    }

    public void update(boolean verbose) {
        if (gamePadState.y) {
           yarm.launch = true;
        }
        else {
            yarm.launch = false;
        }

        if (gamePadState.dPadUp) { // Ready position
            yarm.joint1Target = Yarm.degreesToTicks(80);
            yarm.joint2Target = Yarm.degreesToTicks(-120);
        }

        if (gamePadState.dPadLeft) { // Pull
            yarm.joint1Target = Yarm.degreesToTicks(50);
            yarm.joint2Target = Yarm.degreesToTicks(-50);
        }

        if (gamePadState.dPadDown) { // Rest
            yarm.joint1Target = Yarm.degreesToTicks(0);
            yarm.joint2Target = Yarm.degreesToTicks(0);
        }

        /*
        if (gamePadState.dPadUp) {
            yarm.joint1Velocity = VELOCITY;
        }
        else if (gamePadState.dPadDown) {
            yarm.joint1Velocity = -VELOCITY;
        }
        else {
            yarm.joint1Velocity = 0;
        }

        if (gamePadState.y) {
            yarm.joint2Velocity = VELOCITY;
        }
        else if (gamePadState.a) {
            yarm.joint2Velocity = -VELOCITY;
        }
        else {
            yarm.joint2Velocity = 0;
        }

        if (gamePadState.x) {
            record();
        }

         */

        if (verbose) {
            telemetry.addData("Target1 ", yarm.joint1Target);
            telemetry.addData("Target2 ", yarm.joint2Target);

            for (int i = 0; i < joint1Record.size(); i++) {
                telemetry.addData("--------------------- ", i);
                telemetry.addData("Joint 1 ticks", joint1Record.get(i));
                telemetry.addData("Joint 2 ticks", joint2Record.get(i));
            }
        }
    }

    public void record() {
        if (checkTime.seconds() > 1) {
            joint1Record.add(yarm.joint1Ticks);
            joint2Record.add(yarm.joint2Ticks);
            checkTime.reset();
        }
    }
}
