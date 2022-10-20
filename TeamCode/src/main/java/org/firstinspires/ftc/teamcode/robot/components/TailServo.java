package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

public class TailServo {
    Servo tail;
    boolean inUprightPosition;
    int encoderOffset = 0;
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


}
