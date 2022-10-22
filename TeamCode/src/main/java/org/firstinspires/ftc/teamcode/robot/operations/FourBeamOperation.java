package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.FourBeamMotor;

import java.util.Date;
import java.util.Locale;

public class FourBeamOperation extends Operation {
    FourBeamMotor fourBeam;
    boolean clockwise;
    Type type;

    public enum Type {
        Level_Top,
        Level_Bottom,
    }

    public FourBeamOperation(FourBeamMotor fourBeam, Type type, String title) {
        this.fourBeam = fourBeam;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Fourbeam: Type: %s --%s",
                this.type,
                this.title);
    }

    //Use Limit Switches?

    public boolean isComplete() {
        if (new Date().getTime() - getStartTime().getTime() > RobotConfig.SERVO_REQUIRED_TIME) {
            return true;
        }
        return false;
    }

    public void setFourBeamPosition() {
        switch (type){
            case Level_Top:
                fourBeam.setToTopPosition();
                break;
            case Level_Bottom:
                fourBeam.setToBottomPosition();
                break;
        }
    }

    @Override
    public void startOperation() {
        switch (type) {
            case Level_Bottom: {
                fourBeam.setToTopPosition();
                break;
            }
            case Level_Top: {
                fourBeam.setToBottomPosition();
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        fourBeam.stop();
    }
}
