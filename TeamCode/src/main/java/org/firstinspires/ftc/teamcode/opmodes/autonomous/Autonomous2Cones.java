package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToPositionOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;

public abstract class Autonomous2Cones extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Move forward position");
        //raise cone to high level
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.High, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_POSITION, "Go high"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getReleaseTrajectory(),
                robot.getDriveTrain(),
                "Move forward",
                telemetry
        ));
        state.addPrimaryOperation(new BearingOperation(
                field.getDepositConePose().getHeading(),
                robot.getDriveTrain(),
                "Lineup to deposit held cone",
                telemetry));
        states.add(state);

        state = new State("Deposit loaded cone");
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_DUNK_POSITION, "Dunk cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Release loaded cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_POSITION, "Go back High"));
        states.add(state);

        state = new State("Second cone");
        state.addPrimaryOperation(new BearingOperation(
                field.getHeadingForNavigation(), robot.getDriveTrain(), "Rotate to pick from stack", telemetry));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_STACK_INTERIM_POSITION, "Interim stack pickup position"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_STACK_5_POSITION, "Stack pickup position"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Close, "Close on stack"));
        for (int i=0; i < 10; i++) {
            state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.LowerShoulder, "Lower shoulder"));
            state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.LowerElbow, "Lower elbow"));
        }
        //raise cone to high level
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.High, "Go High"));
        state.addPrimaryOperation(new BearingOperation(
                field.getDepositConePose().getHeading(),
                robot.getDriveTrain(),
                "Lineup to deposit picked up cone",
                telemetry));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Position, RobotConfig.ARM_HIGH_JUNCTION_AUTONOMOUS_DUNK_POSITION, "Dunk cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Release loaded cone"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.High, "Go High"));
        state.addPrimaryOperation(new BearingOperation(
                field.getHeadingForNavigation(),
                robot.getDriveTrain(),
                "Lineup for test",
                telemetry));



        //states.add(state);

        state = new State("Navigate");
        state.addPrimaryOperation(new FollowTrajectory(field.getReturnToReleaseTrajectory(), robot.getDriveTrain(),
                "Get back to center of top tile", telemetry));
        state.addPrimaryOperation(new FollowTrajectory(field.getInterimNavigationTrajectory(), robot.getDriveTrain(),
                "Get to middle tile to navigate", telemetry));
        Trajectory navigationTrajectory = field.getNavigationTrajectory(match.getSignalNumber());
        if (navigationTrajectory != null) {
            state.addPrimaryOperation(new BearingOperation(field.getHeadingForNavigation(), robot.getDriveTrain(), "Turn sideways", telemetry));
            state.addPrimaryOperation(new FollowTrajectory(navigationTrajectory, robot.getDriveTrain(), "Get to right tile", telemetry));
            state.addPrimaryOperation(new BearingOperation(Math.toDegrees(field.getStartingPose().getHeading()), robot.getDriveTrain(), "Rotate to fit", telemetry));
        }
        state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Compact, "Go compact"));
        //states.add(state);
    }
}
