 package org.firstinspires.ftc.teamcode.robot;

 import org.firstinspires.ftc.teamcode.game.Field;
 import org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansDriveConstants;
 import org.firstinspires.ftc.teamcode.robot.components.ArmPosition;

 public class RobotConfig {
    //drive train motors
    public static final String LEFT_FRONT_DRIVE = "leftFrontDrive";
    public static final String LEFT_REAR_DRIVE = "leftRearDrive";
    public static final String RIGHT_REAR_DRIVE = "rightRearDrive";
    public static final String RIGHT_FRONT_DRIVE = "rightFrontDrive";
    public static final String WEBCAM_ID = "Webcam 1";
    public static final String BLINKIN = "blinkin";

    public static final String SHOULDER = "shoulder";
    public static final String ELBOW = "elbow";
    public static final String WRIST = "wrist";
    public static final String ROTATOR = "rotator";
    public static final String CLAW = "claw";

    public static final double CLAW_OPEN_POSITION = 0.6;
    public static final double CLAW_CLENCH_POSITION = 0.92;
    public static final double CLAW_INCREMENT = .01;

    public static final double ROTATOR_INITIAL_POSITION = 0.178;
    public static final double ROTATOR_TURNED_OVER_POSITION = .86;
    public static final double ROTATOR_INCREMENT = .01;

    //Robot center from back
    public static double ROBOT_CENTER_FROM_BACK = (2.25 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    //Robot center from front
    public static double ROBOT_CENTER_FROM_FRONT = (2.25 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ROBOT_WIDTH = 14.5 * Field.MM_PER_INCH;

    public static double ROBOT_CENTER_FROM_VSLAM = ROBOT_LENGTH/2 - (4.5 * Field.MM_PER_INCH);

    //For the high goal, Robot center from held cone is 15.5 inches from the center of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = 21 * Field.MM_PER_INCH;

    public static final double ALLOWED_BEARING_ERROR = 0.75;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final int WRIST_INITIAL_POSITION = 0;
    public static final int WRIST_RELEASED_POSITION = 300;

    public static final int ACCEPTABLE_WRIST_ERROR = 10;
    public static final double MAX_WRIST_POWER = 1.0;
    public static final double DRIVERS_WRIST_POWER = .5;

    public static final int ACCEPTABLE_ELBOW_ERROR = 5;
    public static final double MAX_ELBOW_POWER = 1.0;
    public static final int ELBOW_INCREMENT = 10;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 5;
    public static final double MAX_SHOULDER_POWER = 1.0;
    public static final int SHOULDER_INCREMENT = 20;

    public static final ArmPosition ARM_INTERIM_PICKUP_POSITION = new ArmPosition(6800, 500, 90, ROTATOR_TURNED_OVER_POSITION);
    public static final ArmPosition ARM_PICKUP_POSITION = new ArmPosition(7000, 850, 70, ROTATOR_TURNED_OVER_POSITION);

    public static final ArmPosition ARM_COMPACT_POSITION = new ArmPosition(100, 100, 250, ROTATOR_INITIAL_POSITION);

    public static final ArmPosition ARM_LOW_JUNCTION_POSITION = new ArmPosition(430, 30, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_MIDDLE_JUNCTION_POSITION = new ArmPosition(1290, 560, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_HIGH_JUNCTION_POSITION = new ArmPosition(2650, 1040, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);

    public static final ArmPosition ARM_HIGH_JUNCTION_AUTONOMOUS_POSITION = new ArmPosition(2870, 950, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_HIGH_JUNCTION_AUTONOMOUS_DUNK_POSITION = new ArmPosition(2870, 1400, 125, ROTATOR_INITIAL_POSITION);

    public static final ArmPosition ARM_STACK_5_POSITION = new ArmPosition(5920, 1500, 156, ROTATOR_TURNED_OVER_POSITION);
    public static final ArmPosition ARM_STACK_INTERIM_POSITION = new ArmPosition(3680, 1350, 156, ROTATOR_TURNED_OVER_POSITION);
 }