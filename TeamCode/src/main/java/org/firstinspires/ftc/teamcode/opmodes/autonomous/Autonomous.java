package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansMecanumDrive;
import org.firstinspires.ftc.teamcode.robot.components.Arm;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;

public abstract class Autonomous extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Grab Preloaded Cone");
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Close, "Close claw"));
        state.addPrimaryOperation(new WaitOperation(1000, "Wait a sec"));
        //state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Low, "Raise enough to clear low pole"));
        states.add(state);

        state = new State("Move forward position");
        //raise cone to high level
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.High_Reversed_Interim, "Go interim high"));
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.High_Reversed, "Go High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getReleaseTrajectory(),
                robot.getDriveTrain(),
                "Move forward",
                telemetry
        ));
        double bearingToDepositCone = 0;
        if (match.getAlliance() == Alliance.Color.RED && match.getStartingPosition() == Field.StartingPosition.Right) {
            bearingToDepositCone = -45.0;
        }
        if (match.getAlliance() == Alliance.Color.RED && match.getStartingPosition() == Field.StartingPosition.Left) {
            bearingToDepositCone = 225.0;
        }
        if (match.getAlliance() == Alliance.Color.BLUE && match.getStartingPosition() == Field.StartingPosition.Right) {
            bearingToDepositCone = 135.0;
        }
        if (match.getAlliance() == Alliance.Color.BLUE && match.getStartingPosition() == Field.StartingPosition.Left) {
            bearingToDepositCone = 45.0;
        }

        state.addPrimaryOperation(new BearingOperation(
                bearingToDepositCone,
                robot.getDriveTrain(),
                "Rotate to deposit cone",
                telemetry
        ));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Release cone"));
        states.add(state);


        state = new State("Navigate");
        Trajectory navigationTrajectory = field.getNavigationTrajectory(match.getSignalNumber());
        if (navigationTrajectory != null) {
            state.addPrimaryOperation(new BearingOperation(
                    match.getAlliance() == Alliance.Color.RED ? 0 : 180,
                    robot.getDriveTrain(),
                    "Rotate to travel in channel",
                    telemetry
            ));
            state.addPrimaryOperation(new FollowTrajectory(navigationTrajectory, robot.getDriveTrain(), "Get to right tile", telemetry));
        }
        state.addPrimaryOperation(new BearingOperation(Math.toDegrees(field.getStartingPose().getHeading()), robot.getDriveTrain(), "Rotate to fit", telemetry));
        states.add(state);


        Match.log("Created and added state");
    }
}
