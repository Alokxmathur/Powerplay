package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;

import java.util.Locale;

public class Arm {
    DcMotor shoulder, elbow, wrist;
    Servo rotator, claw;
    int lastShoulderPosition, lastElbowPosition, lastWristPosition;

    public Arm(HardwareMap hardwareMap) {
        //initialize our shoulder motor
        this.shoulder = hardwareMap.get(DcMotor.class, RobotConfig.SHOULDER);
        this.shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //initialize our elbow motor
        this.elbow = hardwareMap.get(DcMotor.class, RobotConfig.ELBOW);
        this.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //initialize our wrist motor
        this.wrist = hardwareMap.get(DcMotor.class, RobotConfig.WRIST);
        this.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //and the rotator
        this.rotator = hardwareMap.get(Servo.class, RobotConfig.ROTATOR);
        //and the claw
        this.claw = hardwareMap.get(Servo.class, RobotConfig.CLAW);

        ensureMotorDirections();
        assumeInitialPosition();
    }

    public void ensureMotorDirections() {
        this.elbow.setDirection(DcMotorSimple.Direction.REVERSE);
        this.shoulder.setDirection(DcMotorSimple.Direction.FORWARD);
        this.wrist.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void assumeInitialPosition() {
        this.rotator.setPosition(RobotConfig.ROTATOR_INITIAL_POSITION);
        this.claw.setPosition(RobotConfig.CLAW_OPEN_POSITION);
        this.wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setWristPosition(RobotConfig.WRIST_INITIAL_POSITION);
        setElbowPosition(0);
        setShoulderPosition(0);
    }

    public void raiseShoulderIncrementally() {
        setShoulderPosition(shoulder.getTargetPosition() + RobotConfig.SHOULDER_INCREMENT);
    }
    public void lowerShoulderIncrementally() {
        setShoulderPosition(shoulder.getTargetPosition() - RobotConfig.SHOULDER_INCREMENT);
    }
    public void raiseElbowIncrementally() {
        setElbowPosition(elbow.getTargetPosition() + RobotConfig.ELBOW_INCREMENT);
    }
    public void lowerElbowIncrementally() {
        setElbowPosition(elbow.getTargetPosition() - RobotConfig.ELBOW_INCREMENT);
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

    public void forwardRotator() {
        this.rotator.setPosition(RobotConfig.ROTATOR_INITIAL_POSITION);
    }
    public void backwardRotator() {
        this.rotator.setPosition(RobotConfig.ROTATOR_TURNED_OVER_POSITION);
    }

    public void backwardRotatorIncrementally() {
        this.rotator.setPosition(rotator.getPosition() + RobotConfig.ROTATOR_INCREMENT);
    }

    public void forwardRotatorIncrementally() {
        this.rotator.setPosition(rotator.getPosition() - RobotConfig.ROTATOR_INCREMENT);
    }

    public void stop() {
    }

    public void setPositions(ArmOperation.Type type) {
        switch (type) {
            case Release: {
                releaseWrist();
                break;
            }
            case Pickup: {
                setPositions(RobotConfig.ARM_PICKUP_POSITION);
                break;
            }
            case InterimDeposit: {
                setPositions(RobotConfig.ARM_INTERIM_DEPOSIT_POSITION);
                break;
            }
            case InterimPickup: {
                setPositions(RobotConfig.ARM_INTERIM_PICKUP_POSITION);
                break;
            }
            case High: {
                setPositions(RobotConfig.ARM_HIGH_JUNCTION_POSITION);
                break;
            }
            case ObjectFinder: {
                setPositions(RobotConfig.OBJECT_FINDER_POSITION);
                break;
            }
            default : {
                Match.log("Nothing done for arm operation of type: " + type);
            }
        }
    }

    private void setPositions(ArmPosition armPosition) {
        rotator.setPosition(armPosition.getRotator());
        setWristPosition(armPosition.getWrist());
        setShoulderPosition(armPosition.getShoulder());
        setElbowPosition(armPosition.getElbow());
    }

    /**
     * Set the shoulder motor position
     * @param position
     */
    public void setShoulderPosition(int position) {
        this.shoulder.setTargetPosition(position);
        this.shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.shoulder.setPower(RobotConfig.MAX_SHOULDER_POWER);
        lastShoulderPosition = position;
    }

    /**
     * Retain shoulder in its current position
     */
    public void retainShoulder() {
        setShoulderPosition(lastShoulderPosition);
    }

    /**
     * Set the shoulder power
     * @param power
     */
    public void setShoulderPower(double power) {
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shoulder.setPower(power);
        lastShoulderPosition = shoulder.getCurrentPosition();
    }

    /**
     * Set the elbow position
     * @param position
     */
    public void setElbowPosition(int position) {
        this.elbow.setTargetPosition(position);
        this.elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.elbow.setPower(RobotConfig.MAX_ELBOW_POWER);
        lastElbowPosition = position;
    }

    /**
     * Retain elbow in its current position
     */
    public void retainElbow() {
        setElbowPosition(lastElbowPosition);
    }

    /**
     * Set the elbow power
     * @param power
     */
    public void setElbowPower(double power) {
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.elbow.setPower(power);
        lastElbowPosition = elbow.getCurrentPosition();
    }


    /**
     * Set the wrist motor position
     * @param position
     */
    public void setWristPosition(int position) {
        this.wrist.setTargetPosition(position);
        this.wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.wrist.setPower(RobotConfig.MAX_WRIST_POWER);
        lastWristPosition = position;
    }

    /**
     * Retain wrist in its current position
     */
    public void retainWrist() {
        setWristPosition(lastWristPosition);
    }

    /**
     * Set the wrist power
     * @param power
     */
    public void setWristPower(double power) {
        this.wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.wrist.setPower(power);
        lastWristPosition = wrist.getCurrentPosition();
    }

    /**
     * Returns true if wrist, elbow and shoulder are within range
     * @return
     */
    public boolean isWithinRange() {
        Match.log(getStatus());
        return shoulderIsWithinRange() && elbowIsWithinRange() && wristIsWithinRange();
    }

    private boolean shoulderIsWithinRange() {
        return Math.abs(shoulder.getTargetPosition() - shoulder.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_SHOULDER_ERROR;
    }

    private boolean elbowIsWithinRange() {
        return Math.abs(elbow.getTargetPosition() - elbow.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_ELBOW_ERROR;
    }

    private boolean wristIsWithinRange() {
        return Math.abs(wrist.getTargetPosition() - wrist.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_WRIST_ERROR;
    }


    public void releaseWrist() {
        setWristPosition(RobotConfig.WRIST_RELEASED_POSITION);
    }
    public void foldWrist() {
        setWristPosition(RobotConfig.WRIST_INITIAL_POSITION);
    }

    /**
     * Returns the status of the arm
     * Reports the current position, target position and power of the shoulder,
     * current position, target position and power of the elbow,
     * the position of the wrist and the position of the claw
     * @return
     */
    public String getStatus() {
        return String.format(Locale.getDefault(), "S:%d->%d@%.2f, E:%d->%d@%.2f, W:%d->%d@%.2f, R:%.3f, C:%.3f",
                shoulder.getCurrentPosition(), shoulder.getTargetPosition(), shoulder.getPower(),
                elbow.getCurrentPosition(), elbow.getTargetPosition(), elbow.getPower(),
                wrist.getCurrentPosition(), wrist.getTargetPosition(), wrist.getPower(),
                rotator.getPosition(), claw.getPosition());
    }
}
