package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class DriveForDynamicDistanceOperation extends DriveInDirectionOperation {
    boolean reversed;
    double dynamicDistance;
    Telemetry telemetry;
    public DriveForDynamicDistanceOperation(boolean reverse, double heading, double speed, DriveTrain driveTrain, String title, Telemetry telemetry) {
        super(0, heading, speed, driveTrain, title);
        this.reversed = reverse;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DynamicDistance: %.2f(%.2f\")@%.2f --%s",
                this.dynamicDistance, this.dynamicDistance/ Field.MM_PER_INCH, this.speed,
                this.title);
    }


    @Override
    public void startOperation() {
        this.dynamicDistance = Match.getInstance(telemetry).getDistanceTraveledForFreight() * (reversed ? -1 : 1);
        Match.log("Set dynamic distance to " + dynamicDistance);
        super.setDistance(dynamicDistance);
        super.startOperation();
    }

}

