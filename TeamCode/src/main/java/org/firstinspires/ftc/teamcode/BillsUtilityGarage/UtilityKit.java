package org.firstinspires.ftc.teamcode.BillsUtilityGarage;

public class UtilityKit {

    public static double limitToRange(double input, double min, double max){
        if(input > max) return max;
        if(input < min) return min;
        return input;
    }

    public static int limitToRange(int input, int min, int max){
        if(input > max) return max;
        if(input < min) return min;
        return input;
    }

//    static public int driveDistanceToTicks (double distance, UnitOfDistance unitOfDistance) { // cm
//        if (unitOfDistance == UnitOfDistance.CM) {
//            return driveDegreesToTicks(distance * (360 / wheelCirCm));
//        }
//        else if (unitOfDistance == UnitOfDistance.IN) {
//            return driveDegreesToTicks(distance * (360 / wheelCirIn));
//        }
//        else {
//            return 0;
//        }
//    }

//    static public int armDegreesToTicks(double degrees) { return (int)(ticksPerDegreeAtJoint*degrees);}
//
//    static public double armTicksToDegrees(double ticks) {return ticks/ticksPerDegreeAtJoint;}
//
//    static public int grabberDegreesToTicks(double degrees) { return (int)((5281.1)/360*degrees);}

    static public double sin(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.sin(Math.toRadians(n));
        }
        else {
            return Math.sin(n);
        }
    }

    static public double cos(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.cos(Math.toRadians(n));
        }
        else {
            return Math.cos(n);
        }
    }

    static public double tan(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.tan(Math.toRadians(n));
        }
        else {
            return Math.tan(n);
        }
    }

    static public double asin(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.toDegrees(Math.asin(n));
        }
        else {
            return Math.asin(n);
        }
    }

    static public double acos(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.toDegrees(Math.acos(n));
        }
        else {
            return Math.acos(n);
        }
    }

    static public double atan(double n, UnitOfAngle unitOfAngle) {
        if (unitOfAngle == UnitOfAngle.DEGREES) {
            return Math.toDegrees(Math.atan(n)); // todo: better to use atan2
        }
        else {
            return Math.atan(n);
        }
    }

    static public double inToCm(double n) {return n*2.54;}

    static public double cmToIn(double n) {return n/2.54;}
}
