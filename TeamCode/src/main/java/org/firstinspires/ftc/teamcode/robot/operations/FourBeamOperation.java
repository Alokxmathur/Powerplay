package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.components.FourBarMotor;

import java.util.Locale;

public class FourBeamOperation extends Operation {
    FourBarMotor fourBeam;
    Type type;

    public enum Type {
        Level_Top,
        Level_Bottom,
    }

    public FourBeamOperation(FourBarMotor fourBeam, Type type, String title) {
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
        return fourBeam.isWithinRange();
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
