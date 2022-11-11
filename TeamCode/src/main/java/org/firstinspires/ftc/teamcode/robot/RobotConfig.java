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

    public static final String SHOULDER = "shoulder";
    public static final String ELBOW = "elbow";
    public static final String WRIST = "wrist";
    public static final String CLAW = "claw";

    public static final double CLAW_OPEN_POSITION = 0;
    public static final double CLAW_CLENCH_POSITION = 1.0;
    public static final double CLAW_INCREMENT = .01;

    public static final double WRIST_INITIAL_POSITION = 0;
    public static final double WRIST_TURNED_OVER_POSITION = 1.0;
    public static final double WRIST_INCREMENT = .01;

    //Robot center from back is two and half inches and half of track length away
    public static double ROBOT_CENTER_FROM_BACK = (SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    //Robot center from held cone is three inches farther than the rear of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = ROBOT_CENTER_FROM_BACK + 3*Field.MM_PER_INCH;
    //Robot center from front is two and a half inches and half of track length away
    public static double ROBOT_CENTER_FROM_FRONT = (SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static double ROBOT_CENTER_FROM_VSLAM = 2.0 * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 13.5 * Field.MM_PER_INCH;

    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ALLOWED_BEARING_ERROR = 0.5;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final int ACCEPTABLE_ELBOW_ERROR = 10;
    public static final int ELBOW_INCREMENT = 1;
    public static final double ELBOW_POWER = 1.0;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 10;
    public static final int SHOULDER_INCREMENT = 2;
    public static final double SHOULDER_POWER = 1.0;
}
