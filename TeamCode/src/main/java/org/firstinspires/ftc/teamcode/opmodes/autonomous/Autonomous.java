package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
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

        state = new State("Clear starting position");
        //raise cone to high level
        //state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Go High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getTurnaroundTrajectory(),
                "Slide over"
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                "Get to delivery point of loaded cone"
        ));
        states.add(state);

        state = new State("Deliver loaded cone");
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Open Claw"));
        states.add(state);

        state = new State("Reach stack");
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromLoadedConeDeliveryTrajectory(),
                "Retract from loaded cone delivery"
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getPickupConeTrajectory(),
                "Reach pickup area"
        ));
        //state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Ground, "Reach stack level"));
        states.add(state);

        state = new State("Grab second cone");
        //state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        //state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Level High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                "Retract from stack"
        ));

        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverSecondConeTrajectory(),
                "Deliver second cone"
        ));
        //state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));

        states.add(state);

        state = new State("Navigate");


        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromSecondConeDeliveryTrajectory(),
                "Retract from second cone"
        ));

        state.addPrimaryOperation(new FollowTrajectory(
                field.getNavigationTrajectory(match.getSignalNumber()),
                "Reach right tile to navigate"
        ));
        //state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Ground, "Lower"));
        states.add(state);

        Match.log("Created and added state");
    }
}
