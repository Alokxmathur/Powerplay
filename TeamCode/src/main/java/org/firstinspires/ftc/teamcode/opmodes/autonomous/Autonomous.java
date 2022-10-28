package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.ClawOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

public abstract class Autonomous extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Clear starting position");
        //grab cone
        state.addSecondaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        //raise cone to high level
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Go High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getTurnaroundTrajectory(),
                robot.getDriveTrain(),
                "Turn around",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                robot.getDriveTrain(),
                "Get to delivery point of loaded cone",
                telemetry
        ));
        states.add(state);

        state = new State("Deliver loaded cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        states.add(state);

        state = new State("Reach stack");
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromLoadedConeDeliveryTrajectory(),
                robot.getDriveTrain(),
                "Retract from loaded cone delivery",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getPickupConeTrajectory(),
                robot.getDriveTrain(),
                "Reach pickup area",
                telemetry
        ));
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Low, "Reach stack level"));
        states.add(state);

        state = new State("Grab second cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Level High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                robot.getDriveTrain(),
                "Retract from stack",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverSecondConeTrajectory(),
                robot.getDriveTrain(),
                "Deliver second cone",
                telemetry
        ));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        states.add(state);

        state = new State("Navigate");
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromSecondConeDeliveryTrajectory(),
                robot.getDriveTrain(),
                "Retract from second cone",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getNavigationTrajectory(match.getSignalNumber()),
                robot.getDriveTrain(),
                "Reach right tile to navigate",
                telemetry
        ));
        states.add(state);

        Match.log("Created and added state");
    }
}
