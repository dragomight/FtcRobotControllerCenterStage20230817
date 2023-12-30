package org.firstinspires.ftc.teamcode.BillsYarm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class Yarm {
    private static final double DELTA_DISTANCE = 2;
    private static final double DELTA_ANGLE = 10;

    private static final double THRESHOLD_DISTANCE = 1;
    private static final double THRESHOLD_ANGLE = 1;

    private static final double VELOCITY = 3;
    private static final double ANGULAR_VELOCITY = 180;

    private static final double L1 = UtilityKit.cmToIn(2.4*10);
    private static final double L2 = UtilityKit.cmToIn(2.4*7);

    public YarmState state = YarmState.FOLD;
    public YarmMode mode = YarmMode.MOVE_BY_JOINTS;

    public Yoint joint1 = new Yoint(90, -90, -90);
    public Yoint joint2 = new Yoint(180, 0, 90);

    public double joint1Target;
    public double joint2Target;

    public double joint1Velocity;
    public double joint2Velocity;

    public Vector2D endpoint;
    public Vector2D endPointTarget;
    public Vector2D endPointVelocity;

    public double joint3Angle; // DAMNED SERVO

    public boolean launch = false;

    public boolean arrival1;
    public boolean arrival2;

    public Yarm() {
        endpoint = new Vector2D();
        endPointTarget = new Vector2D();
        endPointVelocity = new Vector2D();

        joint1Target = joint1.homeAngle;
        joint2Target = joint2.homeAngle;
    }

    public void calculateEndpoint() {
        endpoint.set(
                L1*UtilityKit.sin(joint1.getAngle(), UnitOfAngle.DEGREES)+L2*UtilityKit.sin(joint1.getAngle()+joint2.getAngle(), UnitOfAngle.DEGREES),
                L1*UtilityKit.cos(joint1.getAngle(), UnitOfAngle.DEGREES)+L2*UtilityKit.cos(joint1.getAngle()+joint2.getAngle(), UnitOfAngle.DEGREES));
    }

    public void updateVelocities() {
        if (mode == YarmMode.MOVE_BY_ENDPOINT) {
            setTargetAnglesByEndpointTarget();

            //Calculate velocity vector for endpoint
            Vector2D delta = endPointTarget.copy();
            delta.subtract(endpoint);

            if (delta.magnitude() < THRESHOLD_DISTANCE) {
                endPointVelocity.set(0, 0);
                arrival1 = true;
                arrival2 = true;
            }
            else {
                arrival1 = false;
                arrival2 = false;

                if (delta.magnitude() > DELTA_DISTANCE) {
                    endPointVelocity = delta.unitVector().multiplyBy(VELOCITY);
                }
                else {
                    endPointVelocity = delta.copy().multiplyBy(2);
                }
            }

            Vector2D velocities = InverseKinematics.thDot(L1, L2, endpoint.getX(), endpoint.getY(), endPointVelocity.getX(), endPointVelocity.getY(), joint1.getAngle(), joint2.getAngle(), UnitOfAngle.DEGREES);
            joint1Velocity = velocities.getX();
            joint2Velocity = velocities.getY();
        }
        else {
            setEndpointTargetByTargetAngles();

            //Calculate velocities for each joint to reach directed point // Alternative route is to run by position
            double delta1 = joint1Target - joint1.getAngle();
            double delta2 = joint2Target - joint2.getAngle();

            if (delta1 < THRESHOLD_ANGLE) {
                arrival1 = true;
                joint1Velocity = 0;
            }
            else {
                arrival1 = false;
                if (delta1 > DELTA_ANGLE) {
                    joint1Velocity = UtilityKit.signum(delta1)*ANGULAR_VELOCITY;
                }
                else {
                    joint1Velocity = delta1*2;
                }
            }

            if (delta2 < THRESHOLD_ANGLE) {
                joint2Velocity = 0;
                arrival2 = true;
            }
            else {
                arrival2 = false;
                if (delta2 > DELTA_ANGLE) {
                    joint2Velocity = UtilityKit.signum(delta2)*ANGULAR_VELOCITY;
                }
                else {
                    joint2Velocity = delta2*2;
                }
            }
        }

        // BONUS
        joint3Angle = 90-joint1.getAngle()-joint2.getAngle();
    }

    private void setTargetAnglesByEndpointTarget() {
        joint1Target = UtilityKit.acos((L1*L1+endpoint.magnitude()-L2*L2)/(2*L1*endpoint.magnitude()), UnitOfAngle.DEGREES);
        joint2Target = UtilityKit.acos((L1*L1-endpoint.magnitude()+L2*L2)/(2*L1*L2), UnitOfAngle.DEGREES);
    }

    private void setEndpointTargetByTargetAngles() {
        endpoint.set(
                L1*UtilityKit.sin(joint1Target, UnitOfAngle.DEGREES)+L2*UtilityKit.sin(joint1Target+joint2Target, UnitOfAngle.DEGREES),
                L1*UtilityKit.cos(joint1Target, UnitOfAngle.DEGREES)+L2*UtilityKit.cos(joint1Target+joint2Target, UnitOfAngle.DEGREES));
    }
}
