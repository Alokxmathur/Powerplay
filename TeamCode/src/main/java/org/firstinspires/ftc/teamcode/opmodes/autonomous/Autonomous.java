package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.WinchMotor;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

public abstract class Autonomous extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Deliver held cone");
        /*
        //Create a state to reach the desired location
        state = new State("Reach Destination");
        state.addPrimaryOperation(new FollowTrajectory(
                field.getLocationTrajectory(match.getSignalNumber()),
                robot.getDriveTrain(),
                "Reach our destination",
                telemetry
                ));
        states.add(state);

         */
        state.addPrimaryOperation(new FollowTrajectory(
                field.getTurnaroundTrajectory(),
                robot.getDriveTrain(),
                "Turn around",
                telemetry
        ));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverLoadedConeTrajectory(),
                robot.getDriveTrain(),
                "Deliver loaded cone",
                telemetry
        ));
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
        state.addPrimaryOperation(
                new WinchOperation(
                        robot.getWinchMotor(),
                        match.getAlliance() != Alliance.Color.RED,
                        "Extend winch"));
        states.add(state);
        Match.log("Created and added state");
    }
}
