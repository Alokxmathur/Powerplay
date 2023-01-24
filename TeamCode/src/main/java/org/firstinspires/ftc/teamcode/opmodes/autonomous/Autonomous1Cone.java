package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToPositionOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;

public abstract class Autonomous1Cone extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Move forward position");
        //raise cone to high level
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_POSITION, "Go high"));
        //move forward
        state.addPrimaryOperation(new FollowTrajectory(
                field.getReleaseTrajectory(),
                robot.getDriveTrain(),
                "Move forward",
                telemetry
        ));
        state.addPrimaryOperation(new BearingOperation(field.getDepositConePose().getHeading(),
                robot.getDriveTrain(), "Lineup to deposit", telemetry));
        states.add(state);

        state = new State("Deposit loaded cone");
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_DUNK_POSITION, "Dunk cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Release loaded cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_POSITION, "Go high"));
        states.add(state);

        state = new State("Navigate");
        state.addPrimaryOperation(new BearingOperation(field.getStartingPose().getHeading(),
                robot.getDriveTrain(), "Lineup to go back one tile", telemetry));
        state.addPrimaryOperation(new FollowTrajectory(field.getInterimNavigationTrajectory(), robot.getDriveTrain(),
                "Get to middle tile to navigate", telemetry));
        Trajectory navigationTrajectory = field.getNavigationTrajectory(match.getSignalNumber());
        if (navigationTrajectory != null) {
            state.addPrimaryOperation(new BearingOperation(field.getHeadingForNavigation(), robot.getDriveTrain(), "Turn sideways", telemetry));
            state.addPrimaryOperation(new FollowTrajectory(navigationTrajectory, robot.getDriveTrain(), "Get to right tile", telemetry));
            state.addPrimaryOperation(new BearingOperation(field.getStartingPose().getHeading(), robot.getDriveTrain(), "Rotate to fit", telemetry));
        }
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Compact, "Go compact"));
        states.add(state);
    }
}
