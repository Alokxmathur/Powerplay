package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.FourBarMotor;
import org.firstinspires.ftc.teamcode.robot.components.WinchMotor;

import java.util.Locale;

public class WinchOperation extends Operation {

    public enum Type {
        Ground, Low, Mid, High, Pickup,
    }
    WinchMotor winch;
    FourBarMotor fourBarMotor;
    Type type;
    boolean startedRaisingFourBar, raisedFourBar, startedMovingWinch, movedWinch, startedLoweringFourBar, loweredFourBar;

    public WinchOperation(WinchMotor winch, FourBarMotor fourBarMotor, Type type, String title) {
        this.winch = winch;
        this.fourBarMotor = fourBarMotor;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "WinchMotor: --%s",
                this.title);
    }

    public boolean isComplete() {
        raisedFourBar = fourBarMotor.isWithinRange();
        if (raisedFourBar) {
            if (!startedMovingWinch) {
                startedMovingWinch = true;
                switch (this.type) {
                    case Pickup: {
                        winch.setPosition(RobotConfig.WINCH_PICKUP_POSITION);
                        break;
                    }
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
            movedWinch = winch.isWithinRange();
            if (movedWinch) {
                switch (this.type) {
                    case Ground:
                    case Pickup: {
                        fourBarMotor.setToBottomPosition();
                        break;
                    }
                }
                return fourBarMotor.isWithinRange();
            }
            return false;
        }
        return  false;
    }

    @Override
    public void startOperation() {
        fourBarMotor.setToTopPosition();
        startedRaisingFourBar = true;
    }

    @Override
    public void abortOperation() {
        winch.stop();
    }
}
