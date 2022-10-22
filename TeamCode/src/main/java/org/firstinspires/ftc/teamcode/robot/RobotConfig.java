package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansDriveConstants;

public class RobotConfig {
    //drive train motors
    public static final String LEFT_FRONT_DRIVE = "leftFrontDrive";
    public static final String LEFT_REAR_DRIVE = "leftRearDrive";
    public static final String RIGHT_REAR_DRIVE = "rightRearDrive";
    public static final String RIGHT_FRONT_DRIVE = "rightFrontDrive";
    public static final String WEBCAM_ID = "Webcam 1";
    public static final String BLINKIN = "blinkin";

    public static final String WINCH = "winch";
    public static final String FOUR_BEAM = "fourBeam";

    public static final String TAIL = "tail";
    public static final double TAIL_INITIAL_POSITION = 0;
    public static final double TAIL_PICKUP_POSITION = .75;
    public static final double TAIL_INCREMENT = .01;
    public static final int FOUR_BEAM_INITIAL_POSITION = 0;
    public static final int FOUR_BEAM_TOP_POSITION = 100;

    public static double ROBOT_CENTER_FROM_BACK = (6.0 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static double ROBOT_CENTER_FROM_FRONT = (2.5 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 13.5 * Field.MM_PER_INCH;
    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ALLOWED_BEARING_ERROR = 0.5;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;
    public static final double SUPER_CAUTIOUS_SPEED = 0.4;
    public static final double REGULAR_SPEED = .8;

    public static final long SERVO_REQUIRED_TIME = 200; //200 milli-seconds for servo to function

    public static final double MAX_WINCH_SPEED = 0.08;
    public static final double WINCH_REQUIRED_TIME = 1000;
    public static final double MAX_FOUR_BEAM_SPEED = 0.1;
}
