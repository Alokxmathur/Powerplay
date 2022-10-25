package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.WinchMotor;

import java.util.Locale;

public class WinchOperation extends Operation {
    public enum Type {
        Ground, Low, Mid, High
    }
    WinchMotor winch;
    Type type;

    public WinchOperation(WinchMotor winch, Type type, String title) {
        this.winch = winch;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "WinchMotor: --%s",
                this.title);
    }

    public boolean isComplete() {
        return winch.isWithinRange();
    }

    @Override
    public void startOperation() {
        switch (this.type) {
            case Ground: {
                winch.setPosition(RobotConfig.WINCH_GROUND_POSITION);
                break;
            }
            case Low: {
                winch.setPosition(RobotConfig.WINCH_LOW_POSITION);
                break;
            }
            case Mid: {
                winch.setPosition(RobotConfig.WINCH_MID_POSITION);
                break;
            }
            case High: {
                winch.setPosition(RobotConfig.WINCH_HIGH_POSITION);
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        winch.stop();
    }
}
