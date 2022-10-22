package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.TailServo;

import java.util.Date;

public class TailOperation extends Operation {
    TailServo tail;
    Type type;

    public enum Type {
        Level_Initial,
        Level_Pickup,
    }

    public TailOperation(TailServo tail, Type type, String pickup) {
        this.tail = tail;
        this.type = type;
    }

    public boolean isComplete() {
        long currentTime = new Date().getTime();
        if (type == Type.Level_Initial || type == Type.Level_Pickup) {
            //for open/close operations: just wait servo turn time to say operation is complete
            return currentTime - getStartTime().getTime() > RobotConfig.SERVO_REQUIRED_TIME;
        }
        //for operations requiring shoulder and elbow movement
        else {
            return false;
        }
    }

    private void setTailPosition() {
            switch (type) {
                case Level_Initial: {
                    tail.setTailPosition(RobotConfig.TAIL_INITIAL_POSITION);
                    break;
                }
                case Level_Pickup: {
                    tail.setTailPosition(RobotConfig.TAIL_PICKUP_POSITION);
                    break;
                }
            }
        }
    @Override
    public void startOperation() {
        tail.setUprightPosition(false);
        switch (type) {
            case Level_Pickup:
            case Level_Initial:{
                tail.setTailPosition(RobotConfig.TAIL_INITIAL_POSITION);
                break;
            }
        }
    }
    @Override
    public void abortOperation() {
        tail.stop();
    }
}