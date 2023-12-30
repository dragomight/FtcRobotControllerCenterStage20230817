package org.firstinspires.ftc.teamcode.BillsYarm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class InverseKinematics {

    /**
     * The central function of IK is to calculate the angular velocity of the base and second joints
     * that are required to a target position and target velocity of the end point of the second
     * arm segment.  L1 and L2 are the lengths of the two arm segments.
     * In the IK coordinates, x is out and y is up.
     */
    public static Vector2D thDot(double l1, double l2, double r, double z, double rDot, double zDot, double th1, double th2, UnitOfAngle unit) {

        double v = UtilityKit.cos(th2, unit);
        double vDot = (z*zDot + r*rDot)/(l1*l2);

        double q2 = th2; //UtilityKit.acos(v, unit);

        double q2Dot = -vDot/Math.sqrt(1.00001-(v*v)) * Math.signum(q2);

        double l2SinQ2 = l2 * UtilityKit.sin(q2, unit); // b
        double l2CosQ2 = l2 * UtilityKit.cos(q2, unit); // c
        double base = (l1 + l2CosQ2);

        //double u = UtilityKit.tan(g-th1, unit); //
        double u = l2SinQ2/(l1+l2CosQ2);
        //double uDot = (q2Dot*l2SinQ2*l2SinQ2)/(l1+l2CosQ2)*(l1+l2CosQ2) + (q2Dot*l2CosQ2)/(l1+l2CosQ2);
        double uDot = q2Dot * l2SinQ2 * l2SinQ2 / (base * base) + q2Dot * l2CosQ2 / base;

        double q1 = th1; //g - UtilityKit.atan2(l2SinQ2, l1+l2CosQ2, unit);
        double q1Dot = (rDot*z - r*zDot)/(z*z + r*r + 0.00001) - uDot/(1.0 + u*u);
        return new Vector2D(q1Dot, q2Dot);
    }
}
