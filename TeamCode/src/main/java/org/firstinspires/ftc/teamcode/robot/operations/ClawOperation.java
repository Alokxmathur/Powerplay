package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.ClawSystem;

import java.util.Date;
import java.util.Locale;

public class ClawOperation extends Operation {

    ClawSystem claw;
    Type type;

    public enum Type {
        Open,
        Close
    }

    public ClawOperation(ClawSystem claw, Type type, String title) {
        this.claw = claw;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Claw: Type: %s --%s",
                this.type,
                this.title);
    }

    private void setClawPosition() {
        switch (type) {
            case Open: {
                claw.setClawPosition(RobotConfig.TAIL_INITIAL_POSITION);
                break;
            }
            case Close: {
                claw.setClawPosition(RobotConfig.TAIL_PICKUP_POSITION);
                break;
            }
        }
        Match.log("Tail State: " + claw.getStatus());
    }

    @Override
    public boolean isComplete() {
        long currentTime = new Date().getTime();
        if (type == Type.Open || type == Type.Close) {
            //for open/close operations: just wait servo turn time to say operation is complete
            return currentTime - getStartTime().getTime() > RobotConfig.SERVO_REQUIRED_TIME;
        }
        else {
            return false;
        }
    }
    @Override
    public void startOperation() {
        setClawPosition();
    }

    @Override
    public void abortOperation() {
        claw.stop();
    }
    }