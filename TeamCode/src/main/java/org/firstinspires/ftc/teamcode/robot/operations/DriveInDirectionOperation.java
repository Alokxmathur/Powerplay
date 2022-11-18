package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

import java.util.Date;
import java.util.Locale;

/**
 * Operation to drive in a particular direction for a particular distance
 *
 */

public class DriveInDirectionOperation extends DriveForDistanceOperation {
    protected double distance;
    protected double speed;
    protected double heading;

    /**
     * Create operation to drive distance in direction
     * @param travelDistance
     * @param heading - direction in radians
     * @param speed
     * @param driveTrain
     * @param title
     */
    public DriveInDirectionOperation(double travelDistance, double heading,
                                     double speed, DriveTrain driveTrain, String title) {
        super(travelDistance, travelDistance, driveTrain, title);
        this.distance = travelDistance;
        this.speed = speed;
        this.heading = Math.toDegrees(heading);
        this.driveTrain = driveTrain;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DriveInDirection: %.2f(%.2f\")@%.2f --%s",
                this.distance, (this.distance / Field.MM_PER_INCH), this.heading,
                this.title);
    }

    public boolean isComplete() {
        double currentBearing = Math.toDegrees(driveTrain.getExternalHeading());
        if (driveTrain.driveTrainWithinRange()) {
            return true;
        } else {
            // adjust relative speed based on heading error.
            double bearingError = AngleUnit.normalizeDegrees(heading - currentBearing);
            double steer = DriveTrain.getSteer(bearingError, DriveTrain.P_DRIVE_COEFFICIENT);

            // if driving in reverse, the motor correction also needs to be reversed
            if (distance < 0)
                steer *= -1.0;
            double speedToUse = new Date().getTime() - this.getStartTime().getTime() < 500 ? 0.1 : speed;
            double leftSpeed = speedToUse - steer;
            double rightSpeed = speedToUse + steer;

            // Normalize speeds if either one exceeds +/- 1.0;
            double max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0) {
                leftSpeed /= max;
                rightSpeed /= max;
            }

            Match.log(String.format(Locale.getDefault(), "Setting power LF:%.2f,LR:%.2f,RF:%.2f,RR%.2f,Err:%.2f", leftSpeed, leftSpeed, rightSpeed, rightSpeed, bearingError));

            driveTrain.setLeftFrontPower(leftSpeed);
            driveTrain.setLeftRearPower(leftSpeed);
            driveTrain.setRightFrontPower(rightSpeed);
            driveTrain.setRightRearPower(rightSpeed);

            return false;
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getDistance() {
        return this.distance;
    }
}

