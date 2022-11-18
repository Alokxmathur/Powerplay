package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.ClawOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveInDirectionOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

/**
 * Autonomous routines for depositing navigating without depositing any cones
 */

public abstract class Autonomous0C extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Grab Preloaded Cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        state.addPrimaryOperation(new WaitOperation(1000, "Wait a sec"));
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Low, "Raise enough to clear low pole"));
        states.add(state);

        state = new State("Navigate");
        Trajectory turnAroundTrajectory = field.getNoConeTurnAroundTrajectory(match.getSignalNumber());
        state.addPrimaryOperation(new FollowTrajectory(
                turnAroundTrajectory,
                robot.getDriveTrain(),
                "Slide over",
                telemetry
        ));
        state.addPrimaryOperation(new DriveInDirectionOperation(
                -1.5*Field.TILE_WIDTH,
                turnAroundTrajectory.end().getHeading(),
                0.5,
                robot.getDriveTrain(),
                "Move to right tile"
        ));
        states.add(state);

        Match.log("Created and added states");
    }
}
