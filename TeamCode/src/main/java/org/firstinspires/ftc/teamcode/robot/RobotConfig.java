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
    public static final String WEBCAM_ID_2 = "Webcam 2";
    public static final String BLINKIN = "blinkin";

    public static final String WINCH = "winch";
    public static final String FOUR_BAR = "fourBeam";
    public static final String TAIL = "tail";
    public static final String CLAW = "claw";
    public static final String ENDSERVO = "endServo";

    public static final double TAIL_INITIAL_POSITION = 0;
    public static final double TAIL_PICKUP_POSITION = .75;
    public static final double TAIL_INCREMENT = .01;
    public static final double CLAW_INITIAL_POSITION = .2;
    public static final double CLAW_CLENCH_POSITION = 1.0;
    public static final double CLAW_INCREMENT = .01;
    public static final int FOUR_BAR_INITIAL_POSITION = 0;
    public static final int FOUR_BAR_TOP_POSITION = 115;
    public static final int FOUR_BAR_RELEASE_POSITION = 60;
    public static final int ACCEPTABLE_FOUR_BAR_ERROR = 10;
    public static final double MAX_FOUR_BAR_SPEED = 1.0;
    public static final int FOUR_BAR_INCREMENT = 2;
    public static final double ENDSERVO_INITIAL_POSITION = 0;
    public static final double MAX_VIRTUAL_FOUR_BAR_SPEED = 1.0;
    public static final int VIRTUAL_FOUR_BAR_INITIAL_POSITION = 0;
    public static final int VIRTUAL_FOUR_BAR_DROP_POSITION = 120;

    //Robot center from back is two and half inches and half of track length away
    public static double ROBOT_CENTER_FROM_BACK = (1.5 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    //Robot center from held cone is one inches farther than the rear of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = ROBOT_CENTER_FROM_BACK + 4.0*Field.MM_PER_INCH;
    //Robot center from front is two and a half inches and half of track length away
    public static double ROBOT_CENTER_FROM_FRONT = (3.325 + SilverTitansDriveConstants.TRACK_LENGTH/2) * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 13.5 * Field.MM_PER_INCH;
    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ALLOWED_BEARING_ERROR = 0.5;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final double MAX_WINCH_SPEED = 1.0;
    public static final int WINCH_PICKUP_POSITION = 0;
    public static final int WINCH_TOP_STACK_PICKUP_POSITION = 800;
    public static final int WINCH_STACK_INCREMENT = 430;
    public static final int WINCH_STACK_ONE_POSITION = 1000;
    public static final int WINCH_STACK_TWO_POSITION = 750;
    public static final int WINCH_STACK_THREE_POSITION = 500;
    public static final int WINCH_STACK_FOUR_POSITION = 250;
    public static final int WINCH_GROUND_POSITION = 500;
    public static final int WINCH_LOW_POSITION = 1290;
    public static final int WINCH_MID_POSITION = 2880;
    public static final int WINCH_HIGH_POSITION = 4500;
    public static final int ACCEPTABLE_WINCH_ERROR = 10;
    public static final int WINCH_INCREMENT = 30;

}
