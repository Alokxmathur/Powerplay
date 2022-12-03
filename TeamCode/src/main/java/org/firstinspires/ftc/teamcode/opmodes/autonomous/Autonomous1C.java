package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.ClawOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveInDirectionOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

/**
 * Autonomous routines for depositing one cone and navigating
 */

public abstract class  Autonomous1C extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Grab Preloaded Cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        state.addPrimaryOperation(new WaitOperation(500, "Wait for the grab"));
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Low, "Raise enough to clear low pole"));
        states.add(state);

        state = new State("Clear starting position");
        //raise cone to high level
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Go High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getTurnaroundTrajectory(),
                robot.getDriveTrain(),
                "Slide over",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                robot.getDriveTrain(),
                "Get to delivery point of loaded cone",
                telemetry
        ));
        states.add(state);

        state = new State("Deliver loaded cone and retract");
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.AutoDrop, "Drop Cone"));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        /*state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFor1CNavigationTrajectory(),
                robot.getDriveTrain(),
                "Retract from loaded cone delivery",
                telemetry
        ));*/
        states.add(state);

        state = new State("Bonus points");
        if (match.getAlliance() == Alliance.Color.RED && match.getStartingPosition() == Field.StartingPosition.Right) {
            state.addPrimaryOperation
                    (new BearingOperation(
                            0,
                            robot.getDriveTrain(),
                            "Align", telemetry));
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            Field.TILE_WIDTH * (match.getSignalNumber() - 1),
                            0,
                            0.5,
                            robot.getDriveTrain(),
                            "Move to proper tile"));
        } else if (match.getAlliance() == Alliance.Color.RED && match.getStartingPosition() == Field.StartingPosition.Left) {
            state.addPrimaryOperation
                    (new BearingOperation(Math.toRadians(180), robot.getDriveTrain(), "Align", telemetry));
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            Field.TILE_WIDTH * (3 - match.getSignalNumber()) + Field.TILE_WIDTH/4,
                            Math.toRadians(180),
                            0.5,
                            robot.getDriveTrain(),
                            "Move to proper tile"));
        } else if (match.getAlliance() == Alliance.Color.BLUE && match.getStartingPosition() == Field.StartingPosition.Right) {
            state.addPrimaryOperation
                    (new BearingOperation(Math.toRadians(180), robot.getDriveTrain(), "Align", telemetry));
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            Field.TILE_WIDTH * (match.getSignalNumber() - 1),
                            Math.toRadians(180),
                            0.5,
                            robot.getDriveTrain(),
                            "Move to proper tile"));
        } else if (match.getAlliance() == Alliance.Color.BLUE && match.getStartingPosition() == Field.StartingPosition.Left) {
            state.addPrimaryOperation
                    (new BearingOperation(0, robot.getDriveTrain(), "Align", telemetry));
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            Field.TILE_WIDTH * (3 - match.getSignalNumber()) + Field.TILE_WIDTH/4,
                            0,
                            0.5,
                            robot.getDriveTrain(),
                            "Move to proper tile"));
        }
        state.addPrimaryOperation(new BearingOperation(field.getTurnaroundTrajectory().start().getHeading(),
                robot.getDriveTrain(), "Rotate to fit", telemetry));

        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Ground, "Lower"));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open claw"));

        states.add(state);

        Match.log("Created and added states");
    }
}
