package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class BearingOperation extends DriveToPositionOperation {
    public static final double MIN_SPEED = 0.2;

    protected double desiredBearing;
    private DriveTrain driveTrain;

    /**
     * Operation to get robot to a particular bearing
     * @param desiredBearing in radians
     * @param driveTrain
     * @param title
     * @param telemetry
     */
    public BearingOperation(double desiredBearing, DriveTrain driveTrain, String title, Telemetry telemetry) {
        super(null, driveTrain, title, telemetry);
        this.title = title;
        this.desiredBearing = Math.toDegrees(desiredBearing);
        this.driveTrain = driveTrain;
    }

    public String toString() {
        return String.format(Locale.getDefault(),"Bearing: %.2f --%s",
                this.desiredBearing, this.title);
    }

    public double getDesiredBearing() {
        return desiredBearing;
    }

    @Override
    public void startOperation() {
        this.driveTrain.handleOperation(this);
    }

}
