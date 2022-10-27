package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.ClawOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

public abstract class Autonomous extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Deliver held cone");
        state.addSecondaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close claw"));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Go High"));
        states.add(state);

        state.addPrimaryOperation(new FollowTrajectory(
                field.getTurnaroundTrajectory(),
                robot.getDriveTrain(),
                "Turn around",
                telemetry
        ));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Level High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                robot.getDriveTrain(),
                "Deliver loaded cone",
                telemetry
        ));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        state.addPrimaryOperation(new WaitOperation(250, "Wait quarter a sec before closing Claw"));
        state.addSecondaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Low, "Level for Stack"));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        states.add(state);

        state = new State("Pickup cone");
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
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        states.add(state);

        state = new State("Deliver second cone");
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Level High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                robot.getDriveTrain(),
                "Retract from stack",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                robot.getDriveTrain(),
                "Retract from stack",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverSecondConeTracetory(),
                robot.getDriveTrain(),
                "Deliver second cone",
                telemetry
        ));
        state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));
        state.addPrimaryOperation(new WaitOperation(250, "Wait quarter a sec before closing Claw"));
        state.addSecondaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Ground, "Level Ground"));
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
