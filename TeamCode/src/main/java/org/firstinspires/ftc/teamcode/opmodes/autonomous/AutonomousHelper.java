package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This class implements the methods to make autonomous happen
 */
public abstract class AutonomousHelper extends OpMode {
    protected Match match;
    protected Robot robot;
    protected Field field;

    ArrayList<State> states = new ArrayList<>();

    Date initStartTime;

    boolean cameraPoseSet = false;
    boolean statesAdded = false;
    //start with assuming that there might be an error when initializing the robot
    boolean initErrorHappened = true;
    String initError = "";

    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init(Telemetry telemetry, Alliance.Color alliance, Field.StartingPosition startingPosition) {
        try {
            initStartTime = new Date();
            cameraPoseSet = false;
            statesAdded = false;

            this.match = Match.getNewInstance();
            match.init();
            Match.log("Match initialized, setting alliance to " + alliance
                    + " and starting position to " + startingPosition);
            match.setAlliance(alliance);
            match.setStartingPosition(startingPosition);
            field = match.getField();

            //initialize field for the alliance and starting position
            field.init(alliance, startingPosition);
            //get our robot and initialize it
            this.robot = match.getRobot();
            Match.log("Initializing robot");
            this.robot.init(hardwareMap, telemetry, match);
            Match.log("Robot initialized");
            telemetry.update();
            initErrorHappened = false;
        }
        catch (Throwable e) {
            RobotLog.logStackTrace(e);
            initError = e.toString();
        }
    }
    /*
     * Code to run repeatedly after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        robot.handleGameControllers(gamepad1, gamepad2);
        if (initErrorHappened) {
            telemetry.addData("State", "Error: " + initError);
        }
        else if (Field.isNotInitialized()) {
            telemetry.addData("State", "Trajectories initializing, please wait. " +
                    (30 - (int)(new Date().getTime() - initStartTime.getTime())/1000));
            telemetry.addData("Position", robot.getVSLAMStatus());
        }
        else if (robot.fullyInitialized()) {
            //int signalNumber = robot.getSignalNumber();
            //match.setSignalNumber(signalNumber);
            if (!robot.havePosition()) {
                telemetry.addData("State", "Waiting for VSLAM.");
                telemetry.addData("Position", robot.getVSLAMStatus());
                telemetry.addData("Signal", String.valueOf(match.getSignalNumber()));
                //Match.log("No position from VSLAM yet");
            }
            else if (!cameraPoseSet) {
                telemetry.addData("State", "Setting position, please wait");
                telemetry.addData("Position", robot.getVSLAMStatus());
                telemetry.addData("Signal", String.valueOf(match.getSignalNumber()));
                robot.setInitialPose(field.getStartingPose());
                cameraPoseSet = true;
                Match.log("Set VSLAM starting pose");
            }
            else {
                double xError = robot.getCurrentX() / Field.MM_PER_INCH - field.getStartingPose().getX();
                double yError = robot.getCurrentY() / Field.MM_PER_INCH - field.getStartingPose().getY();
                double bearingError = (Math.toDegrees(robot.getCurrentTheta())
                        - Math.toDegrees(field.getStartingPose().getHeading())) % 360;
                if ((Math.abs(xError) > RobotConfig.ALLOWED_POSITIONAL_ERROR)
                        || (Math.abs(yError) > RobotConfig.ALLOWED_POSITIONAL_ERROR
                        || (Math.abs(bearingError) > RobotConfig.ALLOWED_BEARING_ERROR))) {
                    String positionError = String.format(Locale.getDefault(),
                            "Position Error, restart app:%s v %s, xErr:%.2f, yErr:%.2f, hErr:%.2fv%.2f=%.2f",
                            field.getStartingPose(),
                            robot.getPosition(),
                            xError,
                            yError,
                            Math.toDegrees(robot.getCurrentTheta()),
                            Math.toDegrees(field.getStartingPose().getHeading()),
                            bearingError);
                    telemetry.addData("State", positionError);
                    telemetry.addData("Position", robot.getVSLAMStatus());
                    telemetry.addData("Signal", String.valueOf(match.getSignalNumber()));
                    //robot.setInitialPose(field.getStartingPose());
                } else {
                    match.updateTelemetry(telemetry, "Ready");
                }
            }
        }
        else {
            telemetry.addData("Status", "Cameras initializing, please wait");
        }
        telemetry.update();
        Thread.yield();
    }

    @Override
    public void start() {
        match.setStart();
    }

    /**
     * We go through our specified desired states in this method.
     * Loop through the states, checking if a state is reached, if it is not reached, queue
     *         it if not already queued
     */
    @Override
    public void loop() {
        /*
        Check states sequentially. Skip over reached states and queue those that have not
        been reached and not yet queued
         */
        for (State state : states) {
            if (!state.isReached(robot)) {
                if (state.isQueued()) {
                    match.updateTelemetry(telemetry,"Attempting " + state.getTitle());
                } else {
                    //queue state if it has not been queued
                    match.updateTelemetry(telemetry,"Queueing " + state.getTitle());
                    Match.log("Queueing state: " + state.getTitle());
                    state.queue(robot);
                }
                break;
            }
        }
        //Match.log("Finished reaching final state of states size = "  + states.size());
    }

    @Override
    public void stop() {
        this.robot.stop();
    }
}