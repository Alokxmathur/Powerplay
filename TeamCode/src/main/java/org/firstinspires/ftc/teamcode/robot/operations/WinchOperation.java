package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.WinchMotor;

import java.util.Date;
import java.util.Locale;

public class WinchOperation extends Operation {
    WinchMotor winch;
    boolean clockwise;

    public WinchOperation(WinchMotor winch, boolean clockwise, String title) {
        this.winch = winch;
        this.clockwise = clockwise;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "WinchMotor: --%s",
                this.title);
    }

    public boolean isComplete() {
        if (new Date().getTime() - getStartTime().getTime() > RobotConfig.WINCH_REQUIRED_TIME) {
            winch.stop();
            return true;
        }
        return false;
    }

    @Override
    public void startOperation() {
        if (clockwise) {
            winch.setSpeed(RobotConfig.MAX_WINCH_SPEED);
        }
        else {
            winch.setSpeed(-RobotConfig.MAX_WINCH_SPEED);
        }
    }

    @Override
    public void abortOperation() {
        winch.stop();
    }
}
