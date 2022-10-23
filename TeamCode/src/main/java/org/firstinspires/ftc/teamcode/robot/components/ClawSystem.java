package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class ClawSystem {
    Servo claw;
    boolean inIntakePosition;
    Object synchronizer = new Object();

    public ClawSystem (HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, RobotConfig.CLAW);

        assumeInitialPosition();
    }

    public void setInIntakePosition(boolean inIntakePosition) {
        synchronized (synchronizer) {
            this.inIntakePosition = inIntakePosition;
        }
    }

    public boolean isInIntakePosition() {
        synchronized (synchronizer) {
            return this.inIntakePosition;
        }
    }

    public void setClawPosition(double position) {
        this.claw.setPosition(position);
    }

    public void assumeInitialPosition() {
        this.claw.setPosition(RobotConfig.CLAW_INITIAL_POSITION);
    }

    public void openClaw() {
        this.claw.setPosition(claw.getPosition() - RobotConfig.CLAW_INCREMENT);
    }
    public void closeClaw() {
        this.claw.setPosition(claw.getPosition() + RobotConfig.CLAW_INCREMENT);
    }

    public void openAuto() {
        this.claw.setPosition(RobotConfig.CLAW_INITIAL_POSITION);
    }

    public void closeAuto() {
        this.claw.setPosition(RobotConfig.CLAW_CLENCH_POSITION);
    }

    public void stop() {
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),"Shoulder:%d->%d@%.2f(Offset:%d),Elbow:%.3f,Lid:%.3f",
                this.claw.getPosition());
    }
}
