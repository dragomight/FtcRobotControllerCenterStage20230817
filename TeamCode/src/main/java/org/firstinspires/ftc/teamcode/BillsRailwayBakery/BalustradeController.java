package org.firstinspires.ftc.teamcode.BillsRailwayBakery;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.RoboMode;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.GlyphidSlammer.GlyphidGuts;

public class BalustradeController {
    private Telemetry telemetry;
    private GlyphidGuts glyphidGuts;
    private LeBalustrade balustrade;

    private GamePadState gamePadState1;
    private GamePadState gamePadState2;

    public BalustradePositionID positionID;

    public BalustradeController() {

    }

    public void initialize(GlyphidGuts glyphidGuts) {
        this.glyphidGuts = glyphidGuts;
        telemetry = glyphidGuts.telemetry;
        balustrade = glyphidGuts.leBalustrade;

        gamePadState1 = glyphidGuts.gamePadState1;
        gamePadState2 = glyphidGuts.gamePadState2;

        positionID = BalustradePositionID.FOLD;
    }

    public void update(boolean verbose) {
        if (glyphidGuts.mode == RoboMode.MANUAL) {
            manualUpdate(verbose);
        }

        checkLimits(verbose);
    }

    private void manualUpdate(boolean verbose) {
        // Controls for GamePad1
        if (gamePadState1.a) {
            positionID = BalustradePositionID.FOLD;
            balustrade.joint.setTargetAngle(80);
            balustrade.extendo.setTargetLength(1);
        }
        else if (gamePadState1.b) {
            positionID = BalustradePositionID.LAUNCH;
            balustrade.joint.setTargetAngle(30);
            balustrade.extendo.setTargetLength(1);
        }
        if (gamePadState1.altMode) {
            balustrade.launch = true;
        }
        else {
            balustrade.launch = false;
        }
        if (gamePadState1.rightTrigger > 0) {
            balustrade.grip = true;
        }
        else {
            balustrade.grip = false;
        }

        // Controls for GamePad2
        if (gamePadState2.rightBumper) { // TODO: Setup control for duo grippers
            //balustrade.grip = false;
        }
        else {
            //balustrade.grip = true;
        }
        if (gamePadState2.altMode) {
            balustrade.engageSirLatch = true;
        }
        else {
            balustrade.engageSirLatch = false;
        }
        if (gamePadState2.dPadLeft) {
            balustrade.rollAngle = 90;
        }
        else if (gamePadState2.dPadUp) {
            balustrade.rollAngle = 0;
        }
        else if (gamePadState2.dPadRight) {
            balustrade.rollAngle = -90;
        }
        // Preset positions
        if (gamePadState2.a) {
            //positionID = BalustradePositionID.FOLD;
            //balustrade.joint.setTargetAngle(85);
            //balustrade.extendo.setTargetLength(1);
        }
        else if (gamePadState2.x) {
            //positionID = BalustradePositionID.PLACE;
            //balustrade.joint.setTargetAngle(-10);
        }
        else if (gamePadState2.b) {
            //positionID = BalustradePositionID.GRAB;
            //balustrade.joint.setTargetAngle(85);
            //balustrade.extendo.setTargetLength(1);
        }
        else if (gamePadState2.y) {
            //positionID = BalustradePositionID.HANG;
            //balustrade.joint.setTargetAngle(5);
            //balustrade.extendo.setTargetLength(1);
        }
        //Stick controls
        balustrade.extendo.incrementTargetLength(-gamePadState2.leftStickY*5);
        balustrade.rAngle -= gamePadState2.rightStickY*10;

        if (verbose) {
            telemetry.addData("Position ID: ", positionID);
            telemetry.addData("Current length: ", balustrade.extendo.currentLength);
            telemetry.addData("Target length: ", balustrade.extendo.targetLength);
            telemetry.addData("Current joint angle: ", balustrade.joint.getAngleDeg());
            telemetry.addData("Target joint angle: ", balustrade.joint.getTargetAngle());
            telemetry.addData("GripA: ", balustrade.grip);
            telemetry.addData("Launch: ", balustrade.launch);
            telemetry.addData("Roll: ", balustrade.rollAngle);
            telemetry.addData("Rotation: ", balustrade.rAngle);
        }
    }

    private void checkLimits(boolean verbose) {
        if (balustrade.balustradeLimit) {
            balustrade.joint.incrementTargetAngle(-1);
        }
        if (balustrade.extendoLimit) {
            balustrade.extendo.incrementTargetLength(1);
        }
    }
}
