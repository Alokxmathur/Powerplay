package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

public abstract class DriveTrainOperation extends Operation {
    protected DriveTrain driveTrain;

    public DriveTrainOperation(DriveTrain driveTrain) {
        super();
        this.driveTrain = driveTrain;
    }

    @Override
    public void abortOperation() {
        try {
            this.driveTrain.stop();
        }
        catch (Throwable e) {}
        Match.log("Aborted " + this.title);
    }
}
