package org.firstinspires.ftc.teamcode.robot.components;

public class ArmPosition {
    public int getShoulder() {
        return shoulder;
    }

    public int getElbow() {
        return elbow;
    }

    public int getWrist() {
        return wrist;
    }

    public double getRotator() {
        return rotator;
    }

    int shoulder, elbow, wrist;
    double rotator;

    public ArmPosition(int shoulder, int elbow, int wrist, double rotator) {
        this.shoulder = shoulder;
        this.elbow = elbow;
        this.wrist = wrist;
        this.rotator = rotator;
    }
}
