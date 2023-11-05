package org.firstinspires.ftc.teamcode.BillsUtilityGarage;

import android.util.Log;

public class Matrix3x3 {
    double[][] m = new double[3][3];

    // returns a zero matrix
    public Matrix3x3(){

    }

    /**
     * Creates a matrix by a set of column vectors
     * @param v1
     * @param v2
     * @param v3
     */
    public Matrix3x3(Vector3D v1, Vector3D v2, Vector3D v3){

        m[0][0] = v1.x;
        m[1][0] = v1.y;
        m[2][0] = v1.z;

        m[0][1] = v2.x;
        m[1][1] = v2.y;
        m[2][1] = v2.z;

        m[0][2] = v3.x;
        m[1][2] = v3.y;
        m[2][2] = v3.z;

    }

    public static Matrix3x3 identity(){
        return new Matrix3x3(new Vector3D(1, 0, 0), new Vector3D(0,1, 0), new Vector3D(0, 0, 1));
    }

    public Matrix3x3(double angle, Vector3D axis){
        setRotation(angle, axis);
    }

    public void copy(Matrix3x3 mat){
        mat.m[0][0] = m[0][0];
        mat.m[1][0] = m[1][0];
        mat.m[2][0] = m[2][0];

        mat.m[0][1] = m[0][1];
        mat.m[1][1] = m[1][1];
        mat.m[2][1] = m[2][1];

        mat.m[0][2] = m[0][2];
        mat.m[1][2] = m[1][2];
        mat.m[2][2] = m[2][2];
    }


    /**
     * Creates a rotation matrix of amount angle in degrees about the normal vector
     * @param angle
     * @param axis
     * @return
     */
    public Matrix3x3 setRotation(double angle, Vector3D axis){
        double cosA = Math.cos(Math.toRadians(angle));
        double sinA = Math.sin(Math.toRadians(angle));

        // first column
        m[0][0] = cosA + axis.x * axis.x * (1-cosA);
        m[1][0] = axis.x * axis.y * (1-cosA) + axis.z * sinA;
        m[2][0] = axis.x * axis.z * (1-cosA) - axis.y * sinA;

        // second column
        m[0][1] = axis.x * axis.y * (1-cosA) - axis.z * sinA;
        m[1][1] = cosA + axis.y * axis.y * (1-cosA);
        m[2][1] = axis.y * axis.z * (1-cosA) + axis.x * sinA;

        // third column
        m[0][2] = axis.x * axis.z * (1-cosA) + axis.y * sinA;
        m[1][2] = axis.y * axis.z * (1-cosA) - axis.x * sinA;
        m[2][2] = cosA + axis.z * axis.z * (1-cosA);

        return this;
    }

    public Vector3D multiply(Vector3D v){
        return new Vector3D(
                m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z,
                m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z,
                m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z
        );
    }

//    public Matrix3x3 transpose(){
//        return copy().internalTranspose();
//    }

    public Matrix3x3 transpose(Matrix3x3 target){
        target.m[0][1] = m[1][0];
        target.m[1][0] = m[0][1];
        target.m[0][0] = m[0][0];

        target.m[2][1] = m[1][2];
        target.m[1][2] = m[2][1];
        target.m[1][1] = m[1][1];

        target.m[0][2] = m[2][0];
        target.m[2][0] = m[0][2];
        target.m[2][2] = m[2][2];

        return target;
    }

//    public Matrix3x3 internalTranspose(){
//        double swap;
//        swap = m[1][0];
//        m[1][0] = m[0][1];
//        m[0][1] = swap;
//
//        swap = m[0][2];
//        m[0][2] = m[2][0];
//        m[2][0] = swap;
//
//        swap = m[2][1];
//        m[2][1] = m[1][2];
//        m[1][2] = swap;
//        return this;
//    }

    /**
     * Multiplies two matrices into this matrix
     * @param m1
     * @param m2
     * @return
     */
    public Matrix3x3 multiplyIntoThis(Matrix3x3 m1, Matrix3x3 m2){
        return multiply(m1, m2, this);
    }

    public static Matrix3x3 multiply(Matrix3x3 m1, Matrix3x3 m2, Matrix3x3 r){
        r.m[0][0] = m1.m[0][0] * m2.m[0][0] + m1.m[0][1] * m2.m[1][0] + m1.m[0][2] * m2.m[2][0];
        r.m[0][1] = m1.m[0][0] * m2.m[0][1] + m1.m[0][1] * m2.m[1][1] + m1.m[0][2] * m2.m[2][1];
        r.m[0][2] = m1.m[0][0] * m2.m[0][2] + m1.m[0][1] * m2.m[1][2] + m1.m[0][2] * m2.m[2][2];

        r.m[1][0] = m1.m[1][0] * m2.m[0][0] + m1.m[1][1] * m2.m[1][0] + m1.m[1][2] * m2.m[2][0];
        r.m[1][1] = m1.m[1][0] * m2.m[0][1] + m1.m[1][1] * m2.m[1][1] + m1.m[1][2] * m2.m[2][1];
        r.m[1][2] = m1.m[1][0] * m2.m[0][2] + m1.m[1][1] * m2.m[1][2] + m1.m[1][2] * m2.m[2][2];

        r.m[2][0] = m1.m[2][0] * m2.m[0][0] + m1.m[2][1] * m2.m[1][0] + m1.m[2][2] * m2.m[2][0];
        r.m[2][1] = m1.m[2][0] * m2.m[0][1] + m1.m[2][1] * m2.m[1][1] + m1.m[2][2] * m2.m[2][1];
        r.m[2][2] = m1.m[2][0] * m2.m[0][2] + m1.m[2][1] * m2.m[1][2] + m1.m[2][2] * m2.m[2][2];

        return r;
    }

    public static Matrix3x3 multiply(Matrix3x3 m1, Matrix3x3 m2){
        return multiply(m1, m2, new Matrix3x3());
    }

    public Matrix3x3 multiply(Matrix3x3 m2){
        return multiply(this, m2);
    }

    public boolean nearlyEquals(Matrix3x3 r){
        return  minuteDifference(m[0][0], r.m[0][0]) & minuteDifference(m[0][1], r.m[0][1]) & minuteDifference(m[0][2],r.m[0][2]) &
                minuteDifference(m[1][0], r.m[1][0]) & minuteDifference(m[1][1], r.m[1][1]) & minuteDifference(m[1][2], r.m[1][2]) &
                minuteDifference(m[2][0], r.m[2][0]) & minuteDifference(m[2][1], r.m[2][1]) & minuteDifference(m[2][2], r.m[2][2]);
    }

    public boolean equals(Matrix3x3 r){
        return  m[0][0] == r.m[0][0] & m[0][1] == r.m[0][1] & m[0][2] == r.m[0][2] &
                m[1][0] == r.m[1][0] & m[1][1] == r.m[1][1] & m[1][2] == r.m[1][2] &
                m[2][0] == r.m[2][0] & m[2][1] == r.m[2][1] & m[2][2] == r.m[2][2];
    }

    private static boolean minuteDifference(double a, double b){
        return a - b < .001 || b - a < .001;
    }

    public static void test(){
        Vector3D s1 = new Vector3D(1, 10, 100);
        Vector3D vx = new Vector3D(1, 0, 0); // x unit vector
        Vector3D vy = new Vector3D(0, 1, 0); // y unit vector
        Vector3D vz = new Vector3D(0, 0, 1); // z unit vector
        Matrix3x3 id = Matrix3x3.identity(); // identity matrix
        Matrix3x3 rotX = new Matrix3x3(90, vx); // a 90 degree rotation about the x axis
        Matrix3x3 rotY = new Matrix3x3(90, vy); // a 90 degree rotation about the y axis
        Matrix3x3 rotZ = new Matrix3x3(90, vz); // a 90 degree rotation about the z axis
        Vector3D r1 = rotX.multiply(s1); // should result in (1, -100, 10)
        Vector3D t1 = new Vector3D(1, -100, 10); // the test result
        Vector3D r2 = rotY.multiply(s1);
        Vector3D t2 = new Vector3D(100, 10, -1);
        Vector3D r3 = rotZ.multiply(s1);
        Vector3D t3 = new Vector3D(-10, 1, 100);

        // test the vector normalize function
        Vector3D axis = new Vector3D(1, 1, 1).normalize();
        double mag = axis.magnitude();
        if(mag != 1.0){
            Log.e("Matrix3x3: test", "Vector3D unit vector is not normalized.");
        }

        // test multiplication by an identity matrix
        Matrix3x3 rotA = new Matrix3x3(37, axis);
        Matrix3x3 same = id.multiply(rotA);
        if(!rotA.equals(same)){
            Log.e("Matrix3x3: test", "Multiplication by an identity matrix has failed.");
        }

        // test that the transpose is equal to the inverse by multiplying the transpose by the original to get identity matrix
        Matrix3x3 invRotA = new Matrix3x3();
        rotA.transpose(invRotA);
        Matrix3x3 product = rotA.multiply(invRotA);
//        Log.e("Matrix3x3: test", "rotA = \n" + rotA.toString() + "\n invRotA = \n" + invRotA.toString());
        if(!product.nearlyEquals(id)){
            Log.e("Matrix3x3: test", "error: transpose does not equal inverse \n" + product.toString() + " != \n" + id.toString());
        }

        // test the rotation of a vector about the x axis
        if(!r1.nearlyEqual(t1)){
            Log.e("Matrix3x3: test",  "error: x-axis " + r1.toString() + " != " + t1.toString());
        }

        // test the rotation of a vector about the y axis
        if(!r2.nearlyEqual(t2)){
            Log.e("Matrix3x3: test",  "error: y-axis " + r2.toString() + " != " + t2.toString());
        }

        // test the rotation of a vector about the z axis
        if(!r3.nearlyEqual(t3)){
            Log.e("Matrix3x3: test",  "error: z-axis " + r3.toString() + " != " + t3.toString());
        }

        // test the transpose of the transpose is the original
        Matrix3x3 invR1 = new Matrix3x3();
        rotX.transpose(invR1); // should be the transpose
        Matrix3x3 invInvR1 = new Matrix3x3();
        invR1.transpose(invInvR1); // should be the original
        if(!invInvR1.equals(rotX)){
            Log.e("Matrix3x3: test", "error: transpose failed" + invInvR1.toString() + " != " + rotX.toString());
        }

        // test that the transpose of a rotation matrix is the inverse
        Matrix3x3 result = invR1.multiply(rotX, invR1); // should result in s1
        if(!id.equals(result)){
            Log.e("Matrix3x3: test", "error: transpose is not inverse " + result.toString() + " != " + id.toString());
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%5f %5f %5f]%n", m[0][0], m[0][1], m[0][2]));
        sb.append(String.format("[%5f %5f %5f]%n", m[1][0], m[1][1], m[1][2]));
        sb.append(String.format("[%5f %5f %5f]%n", m[2][0], m[2][1], m[2][2]));
        return sb.toString();
    }
}
