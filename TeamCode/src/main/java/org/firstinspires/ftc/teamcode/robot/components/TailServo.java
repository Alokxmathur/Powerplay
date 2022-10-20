package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class TailServo {
    Servo tail;
    boolean inUprightPosition;
    Object synchronizer = new Object();

    public TailServo(HardwareMap hardwareMap) {
        tail = hardwareMap.get(Servo.class, RobotConfig.TAIL);
    }

    public void setUprightPosition(boolean inUprightPosition) {
        synchronized (synchronizer) {
            this.inUprightPosition = inUprightPosition;
        }
    }

    public boolean isInUprightPosition() {
        synchronized (synchronizer) {
            return this.inUprightPosition;
        }
    }

    public void setTailPosition(double position) {
        this.tail.setPosition(position);
    }

    public void assumeInitialPosition() {
        this.tail.setPosition(RobotConfig.TAIL_INITIAL_POSITION);
    }

    public void changeTailPositionForward() {
        this.tail.setPosition(tail.getPosition() - RobotConfig.TAIL_INCREMENT);
    }

    public void changeTailPositionBackward() {
        this.tail.setPosition(tail.getPosition() + RobotConfig.TAIL_INCREMENT);
    }

    public void stop() {
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),"Tail:%.3f",
                this.tail.getPosition());
    }
}