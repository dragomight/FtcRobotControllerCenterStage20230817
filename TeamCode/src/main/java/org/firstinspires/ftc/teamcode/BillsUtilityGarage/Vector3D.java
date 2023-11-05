package org.firstinspires.ftc.teamcode.BillsUtilityGarage;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D(){}

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3D v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3D multiply(double k){
        return new Vector3D(this.x * k, this.y * k, this.z * k);
    }

    private static boolean minuteDifference(double a, double b){
        return a - b < .001 || b - a < .001;
    }

    public boolean nearlyEqual(Vector3D v){
        return minuteDifference(v.x, this.x) && minuteDifference(v.y, this.y) && minuteDifference(v.z, this.z);
    }

    public static Vector3D add(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public Vector3D add(Vector3D v){
        return new Vector3D(v.x + x, v.y + y, v.z + z);
    }

    public Vector3D subtract(Vector3D v){
        return new Vector3D(-v.x + x, -v.y + y, -v.z + z);
    }

    public Vector3D copy(){
        return new Vector3D(this.x, this.y, this.z);
    }

    public double magnitude(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    // returns a unit vector in the direction of the vector
    public Vector3D unitVector(){
        return copy().normalize();
    }

    public Vector3D normalize(){
        double m = magnitude();
        if(m > 0) {
            x = x / m;
            y = y / m;
            z = z / m;
        }
        else {
            // the default unit vector for zero magnitude
            z = 1;
        }
        return this;
    }

    public String toString(){
        return String.format("[%5f, %5f, %5f]", x, y, z);
    }
}
