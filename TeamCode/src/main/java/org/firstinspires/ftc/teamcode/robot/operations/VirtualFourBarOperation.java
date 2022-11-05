package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.components.FourBarMotor;
import org.firstinspires.ftc.teamcode.robot.components.VirtualFourBar;

import java.util.Locale;

public class VirtualFourBarOperation extends Operation {

    VirtualFourBar virtualFourBar;
    Type type;

    public enum Type {
        Level_Drop,
        Level_Initial,
    }

    public VirtualFourBarOperation(VirtualFourBar virtualFourBar, VirtualFourBarOperation.Type type, String title) {
        this.virtualFourBar = virtualFourBar;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Virtualfourbar: Type: %s --%s",
                this.type,
                this.title);
    }



    @Override
    public boolean isComplete() {
        return virtualFourBar.isWithinRange();
    }

    @Override
    public void startOperation() {
        switch (type) {
            case Level_Drop: {
                virtualFourBar.setToDropOffPosition();
                break;
            }
            case Level_Initial: {
                virtualFourBar.setToPickUpPosition();
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        virtualFourBar.stop();
    }
}
