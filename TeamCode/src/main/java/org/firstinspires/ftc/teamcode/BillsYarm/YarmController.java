package org.firstinspires.ftc.teamcode.BillsYarm;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

import java.util.ArrayList;

public class YarmController {
    Vector2D point1;
    Vector2D point2;

    Telemetry telemetry;
    GamePadState gamePadState;
    Yarm yarm;

    private boolean stick;

    public void initialize(GamePadState gamePadState, Yarm yarm, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.gamePadState = gamePadState;
        this.yarm = yarm;

        point1 = new Vector2D();
        point2 = new Vector2D();
    }

    public void update(boolean verbose) {
        //yarm.launch = gamePadState.y; // TODO: Make a new location for launcher control

        if (gamePadState.altMode) {
            yarm.mode = YarmMode.MOVE_BY_ENDPOINT;

            if (yarm.arrival1 && yarm.arrival2) {
                stick = !stick;
            }

            if (gamePadState.rightBumper) {
                if (stick) {
                    yarm.endPointTarget.set(point1);
                }
                else {
                    yarm.endPointTarget.set(point2);
                }
            }
        }
        else {
            yarm.mode = YarmMode.MOVE_BY_JOINTS;
            if (gamePadState.dPadLeft) {
                yarm.joint1Target = 0;
                yarm.joint2Target = 0;
            }
            else if (gamePadState.dPadRight) {
                yarm.joint1Target = yarm.joint1.homeAngle;
                yarm.joint2Target = yarm.joint2.homeAngle;
            }
            else if (gamePadState.dPadUp) {
                yarm.joint1Target += 5;
            }
            else if (gamePadState.dPadDown) {
                yarm.joint1Target -= 5;
            }

            if (gamePadState.y) {
                yarm.joint2Target += 5;
            }
            else if (gamePadState.a) {
                yarm.joint2Target -= 5;
            }

            if (gamePadState.x) {
                point1 = yarm.endpoint;
            }
            if (gamePadState.b) {
                point2 = yarm.endpoint;
            }
        }

        yarm.updateVelocities();

        if (verbose) {
            telemetry.addData("Target1 ", yarm.joint1Target);
            telemetry.addData("Target2 ", yarm.joint2Target);

            telemetry.addData("Target point: ", yarm.endPointTarget.toString());

            telemetry.addData("Endpoint: ", yarm.endpoint.toString());

            telemetry.addData("Point1: ", point1.toString());
            telemetry.addData("Point2: ", point2.toString());

            telemetry.addData("Arrival1: ", yarm.arrival1);
            telemetry.addData("Arrival2: ", yarm.arrival2);
        }
    }
}
