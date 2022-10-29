package org.firstinspires.ftc.teamcode.robot;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.ClawSystem;
import org.firstinspires.ftc.teamcode.robot.components.FourBarMotor;
import org.firstinspires.ftc.teamcode.robot.components.LED;
import org.firstinspires.ftc.teamcode.robot.components.TailServo;
import org.firstinspires.ftc.teamcode.robot.components.WinchMotor;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.teamcode.robot.components.vision.AprilTagsWebcam;
import org.firstinspires.ftc.teamcode.robot.components.vision.OpenCVWebcam;
import org.firstinspires.ftc.teamcode.robot.components.vision.VslamCamera;
import org.firstinspires.ftc.teamcode.robot.operations.Operation;
import org.firstinspires.ftc.teamcode.robot.operations.OperationThread;
import org.firstinspires.ftc.teamcode.robot.operations.WinchOperation;

/**
 * This class represents our robot.
 *
 * It supports the following controls:
 *  GamePad1:
 *         Left stick - drive, right stick - rotate
 *         x - abort pending operations
 *
 *         a - lowest arm level
 *         b - middle arm level
 *         y - top arm level
 *         Dpad Up - raise intake platform
 *         Dpad Down - lower intake platform
 *         Dpad Left - start intake
 *         Dpad right -
 *              if left trigger is pressed: expel
 *              otherwise - consume freight
 *
 *  GamePad2:
 *         Left stick - y axis - carousel speed
 *
 *         Dpad Up - raise output arm
 *         Dpad Down - lower output arm
 *
 *         Dpad Left -
 *          If right bumper is pressed
 *              Open Lid more
 *          Else
 *              retract output arm
 *         Dpad Right - extend output arm
 *          If right bumper is pressed
 *              Close Lid more
 *          Else
 *              extend output arm
 *
 *         Left trigger -
 *          If right bumper is pressed: open to capping position
 *          else open bucket
 *         Right trigger - close bucket
 *
 *         x - if left bumper is pressed, tell output that this is the correct intake level for intake
 *             otherwise
 *                  go to intake position
 *         a - if left bumper is pressed, tell output that this is the correct intake level for low level
 *             otherwise
 *                  go to lowest arm level
 *         b - if left bumper is pressed, tell output that this is the correct intake level for middle level
 *             otherwise
 *                  go to middle arm level
 *         y - if left bumper is pressed, tell output that this is the correct intake level for top level
 *             otherwise
 *                  go to top arm level
 *
 *
 */

public class Robot {

    Telemetry telemetry;
    private HardwareMap hardwareMap;
    Match match;

    OperationThread operationThreadPrimary;
    OperationThread operationThreadSecondary;
    OperationThread operationThreadTertiary;

    DriveTrain driveTrain;
    LED led;
    WinchMotor winch;
    TailServo tail;
    FourBarMotor fourBeam;
    ClawSystem claw;

    AprilTagsWebcam webcam;
    VslamCamera vslamCamera;

    boolean everythingButCamerasInitialized = false;

    //Our sensors etc.

    //our state
    String state = "pre-initialized";
    public Robot() {
        Log.d("SilverTitans", "Robot: got created");
    }

    /**
     * Initialize our robot
     * We set our alliance and our starting position based on finding a VuMark
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry, Match match) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.match = match;

        //initialize our components
        initCameras();
        initDriveTrain();
        this.led = new LED(hardwareMap);
        this.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);

        this.winch = new WinchMotor(hardwareMap);
        this.tail = new TailServo(hardwareMap);
        this.fourBeam = new FourBarMotor(hardwareMap);
        this.claw = new ClawSystem(hardwareMap);

        telemetry.addData("Status", "Creating operations thread, please wait");
        telemetry.update();

        Match.log("Started operations threads");
        this.operationThreadPrimary = new OperationThread(this, "Primary", telemetry);
        operationThreadPrimary.start();
        this.operationThreadSecondary = new OperationThread(this, "Secondary", telemetry);
        operationThreadSecondary.start();
        this.operationThreadTertiary = new OperationThread(this, "Tertiary", telemetry);
        operationThreadTertiary.start();

        this.everythingButCamerasInitialized = true;
    }

    public void initDriveTrain() {
        //Create our drive train
        telemetry.addData("Status", "Initializing drive train, please wait");
        telemetry.update();
        this.driveTrain = new DriveTrain(hardwareMap, vslamCamera);
    }

    public void initCameras() {
        //initialize Vslam camera
        Match.log("Initializing VSLAM");
        telemetry.addData("Status", "Initializing VSLAM, please wait");
        telemetry.update();
        this.vslamCamera = new VslamCamera(hardwareMap);

        //initialize webcam
        Match.log("Initializing Webcam");
        telemetry.addData("Status", "Initializing Webcam, please wait");
        telemetry.update();
        this.webcam = new AprilTagsWebcam();
        this.webcam.init(hardwareMap, telemetry, OpenCVWebcam.ELEMENT_COLOR_MIN, OpenCVWebcam.ELEMENT_COLOR_MAX);
    }


    /**
     * Stop the robot
     */
    public void stop() {
        //Stop all of our motors
        Match.log("Stopping robot");
        this.operationThreadPrimary.abort();
        this.operationThreadSecondary.abort();
        this.operationThreadTertiary.abort();
        this.driveTrain.stop();
        this.webcam.stop();
        Match.log(("Robot stopped"));
    }

    public void queuePrimaryOperation(Operation operation) {
        this.operationThreadPrimary.queueUpOperation(operation);
    }
    public void queueSecondaryOperation(Operation operation) {
        this.operationThreadSecondary.queueUpOperation(operation);
    }
    public void queueTertiaryOperation(Operation operation) {
        this.operationThreadTertiary.queueUpOperation(operation);
    }

    /**
     * Returns the current x value of robot's center in mms
     * @return the current x position in mms
     */
    public double getCurrentX() {
        return this.vslamCamera.getPoseEstimate().getX()*Field.MM_PER_INCH;
    }

    /**
     * Returns the current y value of robot's center in mms
     * @return the current y position in mms
     */
    public double getCurrentY() {
        return this.vslamCamera.getPoseEstimate().getY()*Field.MM_PER_INCH;
    }

    /**
     * Returns the current heading of the robot in radians
     * @return the heading in radians
     */
    public double getCurrentTheta() {
        return AngleUnit.normalizeRadians(this.vslamCamera.getPoseEstimate().getHeading());}

    public boolean allOperationsCompleted() {
        return primaryOperationsCompleted() && secondaryOperationsCompleted() && tertiaryOperationsCompleted();
    }

    public boolean primaryOperationsCompleted() {
        return !this.operationThreadPrimary.hasEntries();
    }

    public boolean secondaryOperationsCompleted() {
        return !this.operationThreadSecondary.hasEntries();
    }

    public boolean tertiaryOperationsCompleted() {
        return !this.operationThreadTertiary.hasEntries();
    }

    public String getPosition() {
        return this.vslamCamera.getPoseEstimate().toString();
    }

    public boolean havePosition() {
        return vslamCamera.havePosition();
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean fullyInitialized() {
        return this.everythingButCamerasInitialized && this.vslamCamera.isInitialized();
    }

    public void handleGameControllers(Gamepad gamePad1, Gamepad gamePad2) {
        if (gamePad1.x) {
            this.operationThreadPrimary.abort();
            this.operationThreadSecondary.abort();
            this.operationThreadTertiary.abort();
        }

        this.handleDriveTrain(gamePad1);
        handleOutput(gamePad1, gamePad2);
        handleInput(gamePad1, gamePad2);
    }

    public void handleLED(Gamepad gamePad1, Gamepad gamePad2) {

    }

    public void handleDriveTrain(Gamepad gamePad1) {
        if (this.primaryOperationsCompleted()) {
            double multiplier = gamePad1.right_trigger > 0.1 ? .6 : (gamePad1.left_trigger > 0.1 ? 1 : .3);
            double x = Math.pow(gamePad1.left_stick_x, 5) * multiplier; // Get left joystick's x-axis value.
            double y = -Math.pow(gamePad1.left_stick_y, 5) * multiplier; // Get left joystick's y-axis value.

            double rotation = Math.pow(gamePad1.right_stick_x, 5) * multiplier; // Get right joystick's x-axis value for rotation

            this.driveTrain.drive(Math.atan2(x, y), Math.hypot(x, y), rotation);
        }
    }

    public void handleInput(Gamepad gamePad1, Gamepad gamePad2) {

    }

    public void handleOutput(Gamepad gamePad1, Gamepad gamePad2) {

        /*
            gamePad 2 dpad left/right open/close claw totally
        */
        if (gamePad2.dpad_left) {
            claw.openAuto();
        }
        if (gamePad2.dpad_right) {
            claw.closeAuto();;
        }
        /*
            gamePad 2 dpad up/down open/close claw incrementally
        */
        if (gamePad2.dpad_up) {
            claw.openClaw();
        }
        else if (gamePad2.dpad_down) {
            claw.closeClaw();
        }

        if (secondaryOperationsCompleted()) {
        /*
        game pads a, b, y control bucket levels for hub
        if gamePad2 left bumper is pressed, the levels are not set, the output is told that the new
        position for the specified level should be reset

        We don't honor a and b buttons if start is also pressed as this might be when drivers are
        trying to register the controllers
         */
            if (gamePad2.a) {
                queueSecondaryOperation(new WinchOperation(winch, fourBeam, WinchOperation.Type.Ground, "Ground"));
            }
            else if (gamePad2.b) {
                queueSecondaryOperation(new WinchOperation(winch, fourBeam, WinchOperation.Type.Low, "Low"));
            }
            else if (gamePad2.y) {
                queueSecondaryOperation(new WinchOperation(winch, fourBeam, WinchOperation.Type.Mid, "Mid"));
            }
            else if (gamePad2.x) {
                queueSecondaryOperation(new WinchOperation(winch, fourBeam, WinchOperation.Type.High, "High"));
            }
            else if (gamePad1.a) {
                queueSecondaryOperation(new WinchOperation(winch, fourBeam, WinchOperation.Type.Pickup, "Pickup"));
            }
            else if (gamePad2.left_stick_y < -.2) {
                this.winch.raise();
            }
            else if (gamePad2.left_stick_y > .2) {
                this.winch.lower();
            }
            if (gamePad2.right_stick_y < -.2) {
                this.fourBeam.raise();
            }
            else if (gamePad2.right_stick_y > .2) {
                this.fourBeam.lower();
            }
        }
    }

    public void setInitialPose(Pose2d pose) {
        this.driveTrain.setLocalizer(vslamCamera);
        this.vslamCamera.setCurrentPose(pose);
    }

    public void reset() {
        if (this.driveTrain != null) {
            this.driveTrain.ensureWheelDirection();
            this.driveTrain.reset();
        }
        if (this.winch != null) {
            this.winch.ensureDirection();
        }
    }

    public Pose2d getPose() {
        return this.vslamCamera.getPoseEstimate();
    }

    public DriveTrain getDriveTrain() {
        return this.driveTrain;
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        this.led.setPattern(pattern);
    }

    public WinchMotor getWinch() {
        return this.winch;
    }
    public ClawSystem getClaw() {
        return this.claw;
    }
    public FourBarMotor getFourBar() {
        return this.fourBeam;
    }

    public String getTailStatus() {
        return this.tail.getStatus();
    }

    public String getFourBeamStatus() {
        return this.fourBeam.getStatus();
    }

    public String getWinchStatus() {
        return this.winch.getStatus();
    }
    public String getClawStatus() {
        return this.claw.getStatus();
    }
    public String getVSLAMStatus() {
        return this.vslamCamera.getStatus();
    }

    public int getSignalNumber() {
        return this.webcam.getSignalNumber();
    }

    public RevBlinkinLedDriver.BlinkinPattern getLEDStatus() {
        return led.getPattern();
    }
}
