 package org.firstinspires.ftc.teamcode.robot;

 import org.firstinspires.ftc.teamcode.game.Field;

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

    public static final double CLAW_OPEN_POSITION = 0;
    public static final double CLAW_CLENCH_POSITION = 1.0;
    public static final double CLAW_INCREMENT = .01;

    public static final double ROTATOR_INITIAL_POSITION = 0;
    public static final double ROTATOR_TURNED_OVER_POSITION = .77;
    public static final double ROTATOR_INCREMENT = .01;

    //Robot center from back is five and half inches away
    public static double ROBOT_CENTER_FROM_BACK = 5.5 * Field.MM_PER_INCH;
    //Robot center from held cone is three inches farther than the rear of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = ROBOT_CENTER_FROM_BACK + 3*Field.MM_PER_INCH;
    //Robot center from front is four and a half inches
    public static double ROBOT_CENTER_FROM_FRONT = 4.5 * Field.MM_PER_INCH;
    public static double ROBOT_CENTER_FROM_VSLAM = 3.25 * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 14.5 * Field.MM_PER_INCH;

    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ALLOWED_BEARING_ERROR = 0.75;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final double WRIST_INCREMENT = 0.01;
    public static final double WRIST_INITIAL_POSITION = .08;
    public static final double WRIST_RELEASED_POSITION = .85;

    public static final int ACCEPTABLE_ELBOW_ERROR = 10;
    public static final double MAX_ELBOW_POWER = 1.0;
    public static final double DRIVERS_ELBOW_POWER = 0.3;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 10;
    public static final double MAX_SHOULDER_POWER = 1.0;
    public static final double DRIVERS_SHOULDER_POWER = 0.5;
}
