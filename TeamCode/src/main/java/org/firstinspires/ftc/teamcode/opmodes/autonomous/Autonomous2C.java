package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.ClawOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

/**
 * Autonomous routines for depositing two cones and navigating
 */
public abstract class Autonomous2C extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Grab Preloaded Cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        state.addPrimaryOperation(new WaitOperation(1000, "Wait a sec"));
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
        //Not close enough
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                robot.getDriveTrain(),
                "Get to delivery point of loaded cone",
                telemetry
        ));
        states.add(state);

        state = new State("Deliver loaded cone and retract");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromLoadedConeDeliveryTrajectory(),
                robot.getDriveTrain(),
                "Retract from loaded cone delivery",
                telemetry
        ));
        states.add(state);

        state = new State("Reach stack");
        state.addPrimaryOperation(new FollowTrajectory(
                field.getPickupConeTrajectory(),
                robot.getDriveTrain(),
                "Reach pickup area",
                telemetry
        ));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.TopStack, "Reach stack level"));
        states.add(state);

        state = new State("Grab second cone");
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        state.addPrimaryOperation(new WaitOperation(500, "Wait for the grab"));
        state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Lift, "Lift"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                robot.getDriveTrain(),
                "Retract from stack",
                telemetry
        ));
        states.add(state);

        state = new State("Deliver second cone");
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverSecondConeTrajectory(),
                robot.getDriveTrain(),
                "Deliver second cone",
                telemetry
        ));
        states.add(state);

        state = new State("Bonus points");
        state.addSecondaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
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
        Match.log("Created and added states");
    }
}
