package org.firstinspires.ftc.teamcode.game;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.geometry.Rotation2d;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansMecanumDrive.accurateTrajectoryBuilder;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansMecanumDrive.trajectoryBuilder;

/**
 * Created by Silver Titans on 9/16/17.
 */
public class Field {
    public static final float MM_PER_INCH = 25.4f;
    public static final double M_PER_INCH = MM_PER_INCH / 1000;
    //the width of each tile
    public static final double TILE_WIDTH = 24 * MM_PER_INCH;
    // the width of the FTC field (from the center point to the outer panels)
    public static final double FIELD_WIDTH = 6 * TILE_WIDTH;

    public static volatile boolean initialized = false;
    public static final Object mutex = new Object();

    public static final double sqrtOfTwo = Math.sqrt(2);

    public enum StartingPosition {
        Left, Right
    }

    public static final Pose2d redLeftStartingPose = new Pose2d(
            -1.5*TILE_WIDTH / MM_PER_INCH,
            (-FIELD_WIDTH / 2 + RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH,
            Math.toRadians(-90));
    public static final Pose2d redLeftReleasePose = new Pose2d(
            redLeftStartingPose.getX(),
            -(.5 * TILE_WIDTH) / MM_PER_INCH,
            redLeftStartingPose.getHeading());
    public static final Pose2d redLeftDepositPose = new Pose2d(
            -(TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            -(RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(225));
    public static final Pose2d redRightStartingPose =
            new Pose2d(
                    -redLeftStartingPose.getX(),
                    redLeftStartingPose.getY(),
                    Math.toRadians(-90));
    public static final Pose2d redRightReleasePose = new Pose2d(
            redRightStartingPose.getX(),
            -(.5* TILE_WIDTH) / MM_PER_INCH,
            redRightStartingPose.getHeading());
    public static final Pose2d redRightDepositPose = new Pose2d(
            (TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            -(RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(-45));

    public static final Pose2d blueLeftStartingPose =
            new Pose2d(
                    redRightStartingPose.getX(),
                    -redRightStartingPose.getY(),
                    Math.toRadians(90));
    public static final Pose2d blueLeftReleasePose = new Pose2d(
            blueLeftStartingPose.getX(),
            (.5 * TILE_WIDTH) / MM_PER_INCH,
            blueLeftStartingPose.getHeading());
    public static final Pose2d blueLeftDepositPose = new Pose2d(
            (TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));

    public static final Pose2d blueRightStartingPose =
            new Pose2d(
                    -blueLeftStartingPose.getX(),
                    blueLeftStartingPose.getY(),
                    Math.toRadians(90));
    public static final Pose2d blueRightReleasePose = new Pose2d(
            blueRightStartingPose.getX(),
            (.5 * TILE_WIDTH) / MM_PER_INCH,
            blueRightStartingPose.getHeading());
    public static final Pose2d blueRightDepositPose = new Pose2d(
            -(TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(135));

    Trajectory releaseTrajectory, returnToReleaseTrajectory, interimNavigationTrajectory;
    Trajectory navigationTrajectories[] = new Trajectory[3];

    public Trajectory getReleaseTrajectory() {
        return releaseTrajectory;
    }

    private Pose2d startingPose;
    private Pose2d depositConePose;
    Pose2d interimPoseForNavigation;
    double headingForNavigation;

    public void init(Alliance.Color alliance, StartingPosition startingPosition) {
        switch (alliance) {
            case RED: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = redLeftStartingPose;
                        depositConePose = redLeftDepositPose;
                        break;
                    }
                    case Right: {
                        startingPose = redRightStartingPose;
                        depositConePose = redRightDepositPose;
                        break;
                    }
                }
                headingForNavigation = 0;
                break;
            }
            case BLUE: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = blueLeftStartingPose;
                        depositConePose = blueLeftDepositPose;
                        break;
                    }
                    case Right: {
                        startingPose = blueRightStartingPose;
                        depositConePose = blueRightDepositPose;
                        break;
                    }
                }
                headingForNavigation = Math.toRadians(180);
                break;
            }
        }
        Thread initThread = new Thread(() -> {
            Match.log("Field initialization started for "
                    + alliance + ", " + startingPosition.toString());
            createTrajectories(alliance, startingPosition);

            synchronized (mutex) {
                initialized = true;
            }
        });
        initThread.start();
    }

    private void createTrajectories(Alliance.Color alliance, StartingPosition startingPosition) {
        Pose2d releasePose = null;
        switch (alliance) {
            case RED: {
                switch (startingPosition) {
                    case Right: {
                        releasePose = redRightReleasePose;
                        break;
                    }
                    case Left: {
                        releasePose = redLeftReleasePose;
                        break;
                    }
                }
                break;
            }
            case BLUE: {
                switch (startingPosition) {
                    case Right: {
                        releasePose = blueRightReleasePose;
                        break;
                    }
                    case Left: {
                        releasePose = blueLeftReleasePose;
                        break;
                    }
                }
                break;
            }
        } // end switch (alliance)
        releaseTrajectory =
                accurateTrajectoryBuilder(startingPose, true)
                        .splineToConstantHeading(releasePose.vec(), releasePose.getHeading() + Math.toRadians(180.0))
                        .build();
        returnToReleaseTrajectory = trajectoryBuilder(depositConePose, depositConePose.getHeading())
                .splineTo(releasePose.vec(), releasePose.getHeading())
                .build();

        //create navigation trajectories
        if (alliance == Alliance.Color.RED) {
            interimPoseForNavigation = new Pose2d(releasePose.getX(),
                    releasePose.getY() - TILE_WIDTH/MM_PER_INCH, releaseTrajectory.end().getHeading());
        }
        else {
            interimPoseForNavigation = new Pose2d(releasePose.getX(),
                    releasePose.getY() + TILE_WIDTH/MM_PER_INCH, releaseTrajectory.end().getHeading());
        }
        interimNavigationTrajectory = trajectoryBuilder(releasePose, releasePose.getHeading())
                .splineTo(interimPoseForNavigation.vec(), interimPoseForNavigation.getHeading())
                .build();

        navigationTrajectories[0] = accurateTrajectoryBuilder(
                new Pose2d(interimPoseForNavigation.vec(), headingForNavigation), headingForNavigation)
                .forward(-TILE_WIDTH/MM_PER_INCH).build();
        navigationTrajectories[1] = null;
        navigationTrajectories[2] = accurateTrajectoryBuilder(
                new Pose2d(interimPoseForNavigation.vec(), headingForNavigation), headingForNavigation)
                .forward(TILE_WIDTH/MM_PER_INCH).build();
    }

    public Pose2d getDepositConePose() {
        return depositConePose;
    }

    public Pose2d getInterimPoseForNavigation() {
        return interimPoseForNavigation;
    }

    /**
     * Returns the bearing, in radians, for navigating between navigation zones
     * @return
     */
    public double getHeadingForNavigation() {
        return headingForNavigation;
    }

    public Trajectory getReturnToReleaseTrajectory() {
        return returnToReleaseTrajectory;
    }
    public Trajectory getInterimNavigationTrajectory() {
        return interimNavigationTrajectory;
    }
    public Trajectory getNavigationTrajectory(int signalNumber) {
        return navigationTrajectories[signalNumber - 1];
    }

    public static boolean isNotInitialized() {
        synchronized (mutex) {
            return !initialized;
        }
    }

    /**
     * Convert from the poses used by the RoadRunner library to those used by the T265 Camera.
     * The units of measure of the x and y coordinates in the T265 Camera are meters
     * while those of RoadRunner library are inches. The headings are in radians in both cases.
     *
     * @param roadRunnerPose - pose in inches and radians
     * @return pose in meters and radians
     */
    public static com.arcrobotics.ftclib.geometry.Pose2d roadRunnerToCameraPose(Pose2d roadRunnerPose) {
        return new com.arcrobotics.ftclib.geometry.Pose2d
                (roadRunnerPose.getX() * M_PER_INCH, roadRunnerPose.getY() * M_PER_INCH,
                        new Rotation2d(roadRunnerPose.getHeading()));
    }

    /**
     * Convert from the poses used by the T265 Camera and those used by the RoadRunner library.
     * The units of measure of the x and y coordinates in the T265 Camera are meters
     * while those of RoadRunner library are inches. The headings are in radians in both cases.
     *
     * @param cameraPose - pose in meters
     * @return pose in inches
     */
    public static Pose2d cameraToRoadRunnerPose(com.arcrobotics.ftclib.geometry.Pose2d cameraPose) {
        return new Pose2d(
                cameraPose.getTranslation().getX() / M_PER_INCH,
                cameraPose.getTranslation().getY() / M_PER_INCH,
                cameraPose.getHeading());
    }

    public Pose2d getStartingPose() {
        return startingPose;
    }
}