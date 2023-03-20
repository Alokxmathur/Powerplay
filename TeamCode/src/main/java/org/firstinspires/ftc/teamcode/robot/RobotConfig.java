 package org.firstinspires.ftc.teamcode.robot;

 import org.firstinspires.ftc.teamcode.game.Field;
 import org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansDriveConstants;
 import org.firstinspires.ftc.teamcode.robot.components.Arm;
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

    public static final double CLAW_OPEN_POSITION = 0.2;
    public static final double CLAW_CLENCH_POSITION = 0.8;
    public static final double CLAW_INCREMENT = .01;

    public static final double ROTATOR_INITIAL_POSITION = 0.24;
    public static final double ROTATOR_TURNED_OVER_POSITION = .930;
    public static final double ROTATOR_INCREMENT = .01;

    //Robot center from back
    public static double ROBOT_CENTER_FROM_BACK = (2.25 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    //Robot center from front
    public static double ROBOT_CENTER_FROM_FRONT = (2.25 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ROBOT_WIDTH = 14.5 * Field.MM_PER_INCH;

    public static double ROBOT_CENTER_FROM_VSLAM = ROBOT_LENGTH/2 - (4.5 * Field.MM_PER_INCH);

    //For the high goal, Robot center from held cone is twenty inches from the center of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = 20 * Field.MM_PER_INCH;

    public static final double ALLOWED_BEARING_ERROR = 0.75;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final int WRIST_INITIAL_POSITION = 22;
    public static final int WRIST_DEPOSIT_POSITION = 390;
    public static final int WRIST_INTERIM_POSITION = 300;
    public static final int WRIST_RELEASED_POSITION = 390;

    public static final int ACCEPTABLE_WRIST_ERROR = 10;
    public static final double MAX_WRIST_POWER = 0.2;
    public static final double DRIVERS_WRIST_POWER = .5;

    public static final int ACCEPTABLE_ELBOW_ERROR = 10;
    public static final double MAX_ELBOW_POWER = 0.5;
    public static final double DRIVERS_ELBOW_POWER = .5;
    public static final int ELBOW_INCREMENT = 10;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 10;
    public static final double MAX_SHOULDER_POWER = 0.35;
    public static final double DRIVERS_SHOULDER_POWER = 0.35;
    public static final int SHOULDER_INCREMENT = 10;

    public static final ArmPosition ARM_PICKUP_POSITION = new ArmPosition(130, 1450, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_INTERIM_PICKUP_POSITION = new ArmPosition(130, 1010, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_INTERIM_DEPOSIT_POSITION = new ArmPosition(1230, 840, WRIST_INTERIM_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_HIGH_JUNCTION_POSITION = new ArmPosition(1310, 840, WRIST_DEPOSIT_POSITION, ROTATOR_TURNED_OVER_POSITION);

    public static final ArmPosition ARM_HIGH_JUNCTION_REVERSED_POSITION = new ArmPosition(1820, 2780, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_HIGH_JUNCTION_REVERSED_INTERIM_POSITION = new ArmPosition(1620, 0, WRIST_RELEASED_POSITION, ROTATOR_INITIAL_POSITION);
    public static final ArmPosition ARM_MIDDLE_JUNCTION_REVERSED_POSITION = new ArmPosition(1570, 2280, WRIST_DEPOSIT_POSITION, ROTATOR_INITIAL_POSITION);
 }
