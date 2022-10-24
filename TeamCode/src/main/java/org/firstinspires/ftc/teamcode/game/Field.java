package org.firstinspires.ftc.teamcode.game;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.geometry.Rotation2d;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansMecanumDrive.accurateTrajectoryBuilder;

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
            (-Field.TILE_WIDTH - RobotConfig.ROBOT_WIDTH / 2) / MM_PER_INCH,
            (-Field.FIELD_WIDTH / 2 + RobotConfig.ROBOT_CENTER_FROM_BACK) / MM_PER_INCH,
            Math.toRadians(90));
    public static final Pose2d redLeftTurnaroundPose = new Pose2d(
            (-Field.TILE_WIDTH/2) / MM_PER_INCH,
            -(Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH  - 1,
            Math.toRadians(-90));
    public static final Pose2d redLeftDeliverLoadedConePose = new Pose2d(
            (-RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            -(Field.TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(225));
    public static final Pose2d redLeftPickupConeIntermediatePose = new Pose2d(
            (-TILE_WIDTH) / MM_PER_INCH,
            -(Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d redLeftPickupConePose = new Pose2d(
            -(FIELD_WIDTH/2 - RobotConfig.ROBOT_CENTER_FROM_BACK - 120) / MM_PER_INCH,
            -(Field.TILE_WIDTH/2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d redLeftDeliverSecondConePose = new Pose2d(
            -(RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            -(TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));

    public static final Pose2d redRightStartingPose =
            new Pose2d(
                    (Field.TILE_WIDTH + RobotConfig.ROBOT_WIDTH / 2) / MM_PER_INCH,
                    (-Field.FIELD_WIDTH / 2 + RobotConfig.ROBOT_CENTER_FROM_BACK) / MM_PER_INCH,
                    Math.toRadians(90));
    public static final Pose2d redRightTurnaroundPose = new Pose2d(
            (Field.TILE_WIDTH/2) / MM_PER_INCH,
            -(Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH  + 1,
            Math.toRadians(-90));
    public static final Pose2d redRightDeliverLoadedConePose = new Pose2d(
            (RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            -(Field.TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(-45));
    public static final Pose2d redRightPickupConeIntermediatePose = new Pose2d(
            (TILE_WIDTH) / MM_PER_INCH,
            -(Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d redRightPickupConePose = new Pose2d(
            (FIELD_WIDTH/2 - RobotConfig.ROBOT_CENTER_FROM_BACK - 120) / MM_PER_INCH,
            -(Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d redRightDeliverSecondConePose = new Pose2d(
            (TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            -(RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(225));

    public static final Pose2d blueLeftStartingPose =
            new Pose2d(
                    (Field.TILE_WIDTH + RobotConfig.ROBOT_WIDTH / 2) / MM_PER_INCH,
                    (Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_BACK) / MM_PER_INCH,
                    Math.toRadians(-90));
    public static final Pose2d blueLeftTurnaroundPose = new Pose2d(
            (Field.TILE_WIDTH/2) / MM_PER_INCH,
            (Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH  - 1,
            Math.toRadians(90));
    public static final Pose2d blueLeftDeliverLoadedConePose = new Pose2d(
            (RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            (Field.TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));
    public static final Pose2d blueLeftPickupConeIntermediatePose = new Pose2d(
            (TILE_WIDTH) / MM_PER_INCH,
            (Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d blueLeftPickupConePose = new Pose2d(
            (FIELD_WIDTH/2 - RobotConfig.ROBOT_CENTER_FROM_BACK - 120) / MM_PER_INCH,
            (Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d blueLeftDeliverSecondConePose = new Pose2d(
            (TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(135));

    public static final Pose2d blueRightStartingPose =
            new Pose2d(
                    (-Field.TILE_WIDTH - RobotConfig.ROBOT_WIDTH / 2) / MM_PER_INCH,
                    (Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_BACK) / MM_PER_INCH,
                    Math.toRadians(-90));
    public static final Pose2d blueRightTurnaroundPose = new Pose2d(
            -(Field.TILE_WIDTH/2) / MM_PER_INCH,
            (Field.FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH  - 1,
            Math.toRadians(90));
    public static final Pose2d blueRightDeliverLoadedConePose = new Pose2d(
            -(RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            (Field.TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(135));
    public static final Pose2d blueRightPickupConeIntermediatePose = new Pose2d(
            (-TILE_WIDTH) / MM_PER_INCH,
            (Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d blueRightPickupConePose = new Pose2d(
            -(FIELD_WIDTH/2 - RobotConfig.ROBOT_CENTER_FROM_BACK - 120) / MM_PER_INCH,
            (Field.TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d blueRightDeliverSecondConePose = new Pose2d(
            -(TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_BACK/sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));

    Trajectory turnaroundTrajectory;
    public Trajectory getTurnaroundTrajectory() {
        return turnaroundTrajectory;
    }

    Trajectory deliverLoadedConeTrajectory;
    public Trajectory getDeliverLoadedConeTrajectory() {
        return deliverLoadedConeTrajectory;
    }

    Trajectory retractFromLoadedConeDeliveryTrajectory;
    public Trajectory getRetractFromLoadedConeDeliveryTrajectory() {
        return retractFromLoadedConeDeliveryTrajectory;
    }

    Trajectory pickupConeTrajectory;
    public Trajectory getPickupConeTrajectory() {
        return pickupConeTrajectory;
    }

    Trajectory retractFromStackTrajectory;
    public Trajectory getRetractFromStackTrajectory() {
        return retractFromStackTrajectory;
    }

    Trajectory deliverSecondConeTrajectory;
    public Trajectory getDeliverSecondConeTracetory() {
        return deliverSecondConeTrajectory;
    }

    Trajectory retractFromSecondConeDeliveryTrajectory;
    public Trajectory getRetractFromSecondConeDeliveryTrajectory() {
        return retractFromSecondConeDeliveryTrajectory;
    }

    Trajectory navigationTrajectories[] = new Trajectory[3];
    public Trajectory getNavigationTrajectory(int signalNumber) {
        return navigationTrajectories[signalNumber-1];
    }
    /*
    The 12 possible locations for bonus points, three for each starting position (left or right)
    based on the signal number for each alliance
     */
    public static final Pose2d[][][] destinations = {
            {//Red
                    {//Left
                            //Red-left-1
                            new Pose2d(
                                    -2.5 * Field.TILE_WIDTH / MM_PER_INCH,
                                    -Field.TILE_WIDTH / MM_PER_INCH,
                                    Math.toRadians(90)
                            ),
                            //Red-left-2
                            new Pose2d(
                                    -1.5 * Field.TILE_WIDTH / MM_PER_INCH, -Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(90)
                            ),
                            //Red-left-3
                            new Pose2d(
                                    -0.5 * Field.TILE_WIDTH / MM_PER_INCH, -Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(90)
                            )
                    },
                    {//Right
                            //Red-right-1
                            new Pose2d(
                                    .5 * Field.TILE_WIDTH / MM_PER_INCH, -Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(90)
                            ),
                            //Red-right-2
                            new Pose2d(
                                    1.5 * Field.TILE_WIDTH / MM_PER_INCH, -Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(90)
                            ),
                            //Red-right-3
                            new Pose2d(
                                    2.5 * Field.TILE_WIDTH / MM_PER_INCH, -Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(90)
                            )
                    }
            },
            {//Blue
                    {//Left
                            //Blue-left-1
                            new Pose2d(
                                    2.5 * Field.TILE_WIDTH / MM_PER_INCH,
                                    Field.TILE_WIDTH / MM_PER_INCH,
                                    Math.toRadians(-90)
                            ),
                            //Red-left-2
                            new Pose2d(
                                    1.5 * Field.TILE_WIDTH / MM_PER_INCH, Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(-90)
                            ),
                            //Red-left-3
                            new Pose2d(
                                    .5 * Field.TILE_WIDTH / MM_PER_INCH, Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(-90)
                            )
                    },
                    {//Right
                            //Blue-right-1
                            new Pose2d(
                                    -.5 * Field.TILE_WIDTH / MM_PER_INCH, Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(-90)
                            ),
                            //Blue-right-2
                            new Pose2d(
                                    -1.5 * Field.TILE_WIDTH / MM_PER_INCH, Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(-90)
                            ),
                            //Blue-right-3
                            new Pose2d(
                                    -2.5 * Field.TILE_WIDTH / MM_PER_INCH, Field.TILE_WIDTH / MM_PER_INCH, Math.toRadians(-90)
                            )}
            }
    };

    private Pose2d startingPose;
    //our three trajectories based on the signal value
    Trajectory[] locationTrajectory = new Trajectory[3];

    public void init(Alliance.Color alliance, StartingPosition startingPosition) {
        switch (alliance) {
            case RED: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = redLeftStartingPose;
                        break;
                    }
                    case Right: {
                        startingPose = redRightStartingPose;
                        break;
                    }
                }
                break;
            }
            case BLUE: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = blueLeftStartingPose;
                        break;
                    }
                    case Right: {
                        startingPose = blueRightStartingPose;
                        break;
                    }
                }
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
        //create the three trajectories for each signal level
        for (int signal = 0; signal < 3; signal++) {
            Pose2d destinationPose = destinations[alliance.ordinal()][startingPosition.ordinal()][signal];
            Pose2d intermediatePose1, intermediatePose2;
            if (alliance == Alliance.Color.BLUE) {
                intermediatePose1 = new Pose2d(
                        startingPose.getX(),
                        startingPose.getY() - 2,
                        startingPose.getHeading()
                );
                //in case of blue, the intermediate point is one tile length more than the destination
                intermediatePose2 = new Pose2d(
                        destinationPose.getX(),
                        startingPose.getY() - 2,
                        destinationPose.getHeading()
                );
            }
            else {
                intermediatePose1 = new Pose2d(
                        startingPose.getX(),
                        startingPose.getY() + 2,
                        startingPose.getHeading()
                );
                //in case of red, the intermediate point is one tile length less than the destination
                intermediatePose2 = new Pose2d(
                        destinationPose.getX(),
                        startingPose.getY() + 2,
                        destinationPose.getHeading()
                );
            }
            locationTrajectory[signal] = accurateTrajectoryBuilder(startingPose)
                    .splineToConstantHeading(intermediatePose1.vec(), 0)
                    .splineToConstantHeading(intermediatePose2.vec(), 0)
                    .splineToConstantHeading(destinationPose.vec(), 0).build();
            Pose2d turnaroundPose = null;
            Pose2d deliverLoadedConePose = null;
            Pose2d pickConeIntermediatePose = null;
            Pose2d pickupConePose = null;
            Pose2d deliverSecondConePose = null;
            switch (alliance) {
                case RED:
                {
                    switch (startingPosition) {
                        case Right: {
                            turnaroundPose = redRightTurnaroundPose;
                            deliverLoadedConePose = redRightDeliverLoadedConePose;
                            pickConeIntermediatePose = redRightPickupConeIntermediatePose;
                            pickupConePose = redRightPickupConePose;
                            deliverSecondConePose = redRightDeliverSecondConePose;
                            break;
                        }
                        case Left: {
                            turnaroundPose = redLeftTurnaroundPose;
                            deliverLoadedConePose = redLeftDeliverLoadedConePose;
                            pickConeIntermediatePose = redLeftPickupConeIntermediatePose;
                            pickupConePose = redLeftPickupConePose;
                            deliverSecondConePose = redLeftDeliverSecondConePose;
                            break;
                        }
                    }
                    break;
                }
                case BLUE:
                {
                    switch (startingPosition) {
                        case Right: {
                            turnaroundPose = blueRightTurnaroundPose;
                            deliverLoadedConePose = blueRightDeliverLoadedConePose;
                            pickConeIntermediatePose = blueRightPickupConeIntermediatePose;
                            pickupConePose = blueRightPickupConePose;
                            deliverSecondConePose = blueRightDeliverSecondConePose;
                            break;
                        }
                        case Left: {
                            turnaroundPose = blueLeftTurnaroundPose;
                            deliverLoadedConePose = blueLeftDeliverLoadedConePose;
                            pickConeIntermediatePose = blueLeftPickupConeIntermediatePose;
                            pickupConePose = blueLeftPickupConePose;
                            deliverSecondConePose = blueLeftDeliverSecondConePose;
                            break;
                        }
                    }
                    break;
                }
            } // end switch (alliance)
            turnaroundTrajectory =
                    accurateTrajectoryBuilder(startingPose, startingPose.getHeading())
                            .splineTo(turnaroundPose.vec(), turnaroundPose.getHeading())
                            .build();
            deliverLoadedConeTrajectory =
                    accurateTrajectoryBuilder(turnaroundPose, true)
                            .splineTo(deliverLoadedConePose.vec(), deliverLoadedConePose.getHeading()
                                    + Math.toRadians(180))
                            .build();
            retractFromLoadedConeDeliveryTrajectory =
                    accurateTrajectoryBuilder(deliverLoadedConeTrajectory.end(), deliverLoadedConeTrajectory.end().getHeading())
                            .forward(12)
                            .build();
            pickupConeTrajectory = accurateTrajectoryBuilder(retractFromLoadedConeDeliveryTrajectory.end(), true)
                    .splineTo(pickConeIntermediatePose.vec(), pickConeIntermediatePose.getHeading() + Math.toRadians(180))
                    .splineTo(pickupConePose.vec(), pickupConePose.getHeading() + Math.toRadians(180))
                    .build();
            retractFromStackTrajectory = accurateTrajectoryBuilder(pickupConeTrajectory.end(), true)
                    .forward(55)
                    .build();
            deliverSecondConeTrajectory = accurateTrajectoryBuilder(retractFromStackTrajectory.end(), true)
                    .splineTo(deliverSecondConePose.vec(), deliverSecondConePose.getHeading() + Math.toRadians(180))
                    .build();
            retractFromSecondConeDeliveryTrajectory = accurateTrajectoryBuilder(deliverSecondConeTrajectory.end(), deliverSecondConeTrajectory.end().getHeading())
                    .splineTo(retractFromStackTrajectory.end().vec(), retractFromStackTrajectory.end().getHeading())
                    .build();
            for (int i = 0; i < 3; i++) {
                navigationTrajectories[i] = accurateTrajectoryBuilder(retractFromSecondConeDeliveryTrajectory.end(), true)
                        .forward(-(i * TILE_WIDTH / MM_PER_INCH + 7))
                        .build();
            }
        }
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

    public Trajectory getLocationTrajectory(int signal) {
        return locationTrajectory[signal-1];
    }
}