package org.firstinspires.ftc.teamcode.BillsRailwayBakery;

import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsEs.MotorType;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;

public class DCArmJoint {
    private double angle; // joint angle in degrees, read from motor encoder in ticks and converted to degrees
    private double maxRange; // the max angle of this joint in degrees, assumes it is properly zeroed
    private double minRange; // the min angle of this joint in degrees, assumes it is properly zeroed
    private double angularVelocity; // the angular velocity of the joint in degrees per second, as read from the motor encoders
    private double targetAngle; // the target angle in degrees
    private double targetAngularVelocity; // the target angular velocity
    private double homeAngle; // the angle offset in degrees where autohome regards as zero ticks
    public MotorType motorType = MotorType.SPEED;
    public int angularToleranceInTicks = 5; // five ticks??? one degree
    private double maxAngularVelocity = 60; // deg/sec
    private double maxAngularAcceleration = 120; // deg/sec/sec
    private ElapsedTime runtime = new ElapsedTime();

    // Motor stats are for GoBilda 5203-2402-0003 DC motor 3.7:1 ratio, 1620 rpm, where .288 x 28 = 8.06 ticks/degree at the joint
    public final static double PPR_0003 = 103.8; // pulse/revolution at the output shaft of the motor (speed motors)
    public final static double PPR_0019 = 537.7; // ppr (drivetrain motors)
    public final static double PPR_0188 = 5281.1; // ppr (big motors)
    public final static double DEGREE_TOLERANCE = 2;

    public DCArmJoint(double minRange, double maxRange, double homeAngle, MotorType motorType){
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.homeAngle = homeAngle;
        targetAngle = homeAngle;
        this.motorType = motorType;
        if(motorType == MotorType.SPEED)
            this.angularToleranceInTicks = (int)(PPR_0003 * DEGREE_TOLERANCE);
        else if (motorType == MotorType.DRIVETRAIN)
            this.angularToleranceInTicks = (int)(PPR_0019 * DEGREE_TOLERANCE);
        else
            this.angularToleranceInTicks = (int)(PPR_0188 * DEGREE_TOLERANCE);

        runtime.reset();
    }

    public void setTargetToHome(){
        targetAngle = homeAngle;
    }

    public void setTargetToMin(){
        targetAngle = minRange;
    }

    public void setTargetToMax(){
        targetAngle = maxRange;
    }

    // returns the current angle in degrees
    public double getAngleDeg(){
        return angle;
    }

    // get the angular velocity in degrees per second
    public double getAngularVelocityDegPS(){
        return angularVelocity;
    }

    // sets the angle using the number of ticks read from the motor encoder
    public void setAngleByTicks(int ticks){
        angle = armTicksToDegrees(ticks) + homeAngle; // zero ticks corresponds to the home angle after autoHome
    }

    // convert ticks per second to degrees per second
    public void setAngularVelocityByTicksPerSecond(double tps){
        angularVelocity = armTicksToDegrees(tps);
    }

    // sets the target angle in degrees
    public void setTargetAngle(double targetAngle){
        this.targetAngle = UtilityKit.limitToRange(targetAngle, minRange, maxRange);
    }

    // used for autohome, this does not limit the range of angle allowed because the angle is not set yet
    public void incrementTargetAngle(double increment){
        this.targetAngle += increment;
    }

    // set the target angular velocity in degrees per second
    public void setTargetAngularVelocity(double targetAngularVelocity){
        this.targetAngularVelocity = targetAngularVelocity;
    }

    // gets the target angle in ticks
    public double getTargetTicks(){
        return armDegreesToTicks(targetAngle - homeAngle); // when target angle equals the home angle, we are at zero ticks after autoHome
    }

    public double getTargetAngle() {
        return targetAngle;
    }

    // gets the target angular velocity in ticks per second
    public double getTargetTicksPerSecond(){
        return armDegreesToTicks(targetAngularVelocity);
    }

    public String toString(){
        return "angle: " +  angle + " target: " + targetAngle;
    }

    public double armDegreesToTicks(double degrees) {
        if (motorType == MotorType.SPEED) {
            return PPR_0003/360 * degrees;
        } else if (motorType == MotorType.DRIVETRAIN) {
            return PPR_0019/360 * degrees;
        } else {
            return PPR_0188/360 * degrees;
        }
    }

    public double armTicksToDegrees(double ticks) {
        if (motorType == MotorType.SPEED) {
            return ticks /(PPR_0003/360);
        }
        else if (motorType == MotorType.DRIVETRAIN) {
            return ticks / (PPR_0019/360);
        }
        else {
            return ticks/ (PPR_0188/360);
        }
    }

    public double getTargetAngularVelocity(){
        return targetAngularVelocity;
    }

    public double getMaxAngularVelocity(){
        return maxAngularVelocity;
    }

    public void accelerate(double alpha){
        alpha = UtilityKit.limitToRange(alpha, -maxAngularAcceleration, maxAngularAcceleration);
        targetAngularVelocity = targetAngularVelocity + alpha * runtime.seconds();
        targetAngularVelocity = UtilityKit.limitToRange(targetAngularVelocity, -maxAngularVelocity, maxAngularVelocity);
        runtime.reset();
        if(rangeDanger()){
            Log.e("DCArmJoint: accelerate", "range danger!");
        }
        //Log.i("DCArmJoint: accelerate", "targetAngularVelocity=" + targetAngularVelocity);
    }

    public void brake(double alpha){
        // acceleration can only be applied opposite to the direction of motion and never increases the magnitude of angular velocity
        if(targetAngularVelocity > 0) {
            alpha = -Math.abs(alpha);
            alpha = UtilityKit.limitToRange(alpha, -maxAngularAcceleration, 0);
            targetAngularVelocity = targetAngularVelocity + alpha * runtime.seconds();
            targetAngularVelocity = UtilityKit.limitToRange(targetAngularVelocity, 0, maxAngularVelocity);
        }
        else if(targetAngularVelocity < 0){
            alpha = Math.abs(alpha);
            alpha = UtilityKit.limitToRange(alpha, 0, maxAngularAcceleration);
            targetAngularVelocity = targetAngularVelocity + alpha * runtime.seconds();
            targetAngularVelocity = UtilityKit.limitToRange(targetAngularVelocity, -maxAngularVelocity, 0);
        }
        runtime.reset();
    }

    /**
     * brake at a rate between 0 and 1, min and max.  Negative numbers will brake just the same.
     * @param alphaRatio
     */
    public void brakeRate(double alphaRatio){
        brake(maxAngularAcceleration * alphaRatio);
    }

    /**
     * accelerate at a rate between 0 and 1, of the max rate.  Negative numbers accelerate in the opposite direction.
     * @param alphaRatio
     */
    public void accelerationRate(double alphaRatio){
        accelerate(maxAngularAcceleration * alphaRatio);
    }

    /**
     * Just stop, however jerky, as fast as possible.
     */
    public void emergencyStop(){
        targetAngularVelocity = 0;
    }

    public void setMaxAngularVelocity(double maxOmega){
        maxAngularVelocity = maxOmega;
    }

    public void setMaxAngularAcceleration(double maxAlpha){
        maxAngularAcceleration = maxAlpha;
    }

    public static int grabberDegreesToTicks(double degrees) { return (int)((5281.1)/360*degrees);} // todo

    /**
     *  Do an emergency stop and return true if there is a danger of going outside of joint limits
     */
    public boolean rangeDanger(){
        if(angle + angularVelocity * runtime.seconds() > maxRange){
            emergencyStop();
            return true;
        }
        else if(angle + angularVelocity * runtime.seconds() < minRange){
            emergencyStop();
            return true;
        }
        return false;
    }

}
