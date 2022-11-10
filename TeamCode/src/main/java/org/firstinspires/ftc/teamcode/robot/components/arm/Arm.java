package org.firstinspires.ftc.teamcode.robot.components.arm;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;

import java.util.Locale;

public class Arm {
    DcMotor shoulder, elbow;
    Servo wrist, claw;

    public Arm(HardwareMap hardwareMap) {
        this.shoulder = hardwareMap.get(DcMotor.class, RobotConfig.SHOULDER);
        this.shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.elbow = hardwareMap.get(DcMotor.class, RobotConfig.ELBOW);
        this.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.wrist = hardwareMap.get(Servo.class, RobotConfig.WRIST);
        this.claw = hardwareMap.get(Servo.class, RobotConfig.CLAW);

        ensureMotorDirections();
        assumeInitialPosition();
    }

    public void ensureMotorDirections() {
        this.elbow.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setClawPosition(double position) {
        this.claw.setPosition(position);
    }

    public void assumeInitialPosition() {
        this.wrist.setPosition(RobotConfig.WRIST_INITIAL_POSITION);
        this.claw.setPosition(RobotConfig.CLAW_OPEN_POSITION);
    }

    public void openClawIncrementally() {
        this.claw.setPosition(claw.getPosition() - RobotConfig.CLAW_INCREMENT);
    }

    public void closeClawIncrementally() {
        this.claw.setPosition(claw.getPosition() + RobotConfig.CLAW_INCREMENT);
    }

    public void openClaw() {
        this.claw.setPosition(RobotConfig.CLAW_OPEN_POSITION);
    }

    public void closeClaw() {
        this.claw.setPosition(RobotConfig.CLAW_CLENCH_POSITION);
    }

    public void forwardWrist() {
        this.wrist.setPosition(RobotConfig.WRIST_INITIAL_POSITION);
    }
    public void backwardWrist() {
        this.wrist.setPosition(RobotConfig.WRIST_TURNED_OVER_POSITION);
    }

    public void backwardWristIncrementally() {
        this.wrist.setPosition(wrist.getPosition() + RobotConfig.WRIST_INCREMENT);
    }

    public void forwardWristIncrementally() {
        this.wrist.setPosition(wrist.getPosition() - RobotConfig.WRIST_INCREMENT);
    }

    public void stop() {
    }

    public void setPositions(ArmOperation.Type type) {
    }

    /**
     * Set the shoulder motor position
     * @param position
     */
    public void setShoulderPosition(int position) {
        this.shoulder.setTargetPosition(position);
        this.shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.shoulder.setPower(RobotConfig.SHOULDER_POWER);
    }

    /**
     * Set the elbow position
     * @param position
     */
    public void setElbowPosition(int position) {
        this.elbow.setTargetPosition(position);
        this.elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.elbow.setPower(RobotConfig.ELBOW_POWER);
    }

    public boolean isWithinRange() {
        return shoulderIsWithinRange() && elbowIsWithinRange();
    }

    private boolean elbowIsWithinRange() {
        return Math.abs(elbow.getTargetPosition() - elbow.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_ELBOW_ERROR;
    }

    private boolean shoulderIsWithinRange() {
        return Math.abs(shoulder.getTargetPosition() - shoulder.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_SHOULDER_ERROR;
    }

    public void raise() {
        this.shoulder.setTargetPosition(this.shoulder.getTargetPosition() + RobotConfig.SHOULDER_INCREMENT);
        this.elbow.setTargetPosition(this.elbow.getTargetPosition() + RobotConfig.ELBOW_INCREMENT);
    }

    public void lower() {
        this.shoulder.setTargetPosition(this.shoulder.getTargetPosition() - RobotConfig.SHOULDER_INCREMENT);
        this.elbow.setTargetPosition(this.elbow.getTargetPosition() - RobotConfig.ELBOW_INCREMENT);
    }

    /**
     * Returns the status of the arm
     * Reports the current position, target position and power of the shoulder,
     * current position, target position and power of the elbow,
     * the position of the wrist and the position of the claw
     * @return
     */
    public String getStatus() {
        return String.format(Locale.getDefault(), "S:%d->%d@%.2f, E:%d->%d@%.2f, W:%.3f, C:%.3f",
                shoulder.getCurrentPosition(), shoulder.getTargetPosition(), shoulder.getPower(),
                elbow.getCurrentPosition(), elbow.getTargetPosition(), elbow.getPower(),
                wrist.getPosition(), claw.getPosition());
    }
}
