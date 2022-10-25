/*
 * Copyright (c) 2020 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.robot.components.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.AprilTagsPipeline;
import org.opencv.core.Scalar;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.Left;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.Middle;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.Right;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.cx;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.cy;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.fx;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.fy;
import static org.firstinspires.ftc.teamcode.robot.components.vision.pipeline.Constants.tagsize;

public class AprilTagsWebcam {

    public static final int Y_PIXEL_COUNT = 1280;
    public static final int X_PIXEL_COUNT = 720;

    OpenCvWebcam webcam;
    AprilTagsPipeline pipeline;
    Scalar colorMin;
    Scalar colorMax;

    AprilTagDetection tagOfInterest = null;

    Telemetry telemetry;

    public static final Object synchronizer = new Object();

    public void init(HardwareMap hardwareMap, Telemetry telemetry, Scalar colorMin, Scalar colorMax) {
        this.telemetry = telemetry;
        this.colorMin = colorMin;
        this.colorMax = colorMax;
        pipeline = new AprilTagsPipeline(tagsize, fx, fy, cx, cy, telemetry);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM_ID_2), cameraMonitorViewId);
        //pipeline.setTelemetry(telemetry);
        webcam.setPipeline(pipeline);
        start();
    }

    public void start() {
        Match.log("Attempting to start " + RobotConfig.WEBCAM_ID_2 + " usage");

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        //webcam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.showFpsMeterOnViewport(false);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                Match.log("Opened " + RobotConfig.WEBCAM_ID);

                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                webcam.startStreaming(Y_PIXEL_COUNT, X_PIXEL_COUNT, OpenCvCameraRotation.SIDEWAYS_LEFT);
                Match.log("Streaming started on " + RobotConfig.WEBCAM_ID);
            }

            @Override
            public void onError(int errorCode) {
                Match.log("Unable to open " + RobotConfig.WEBCAM_ID);
            }
        });
        //FtcDashboard.getInstance().startCameraStream(webcam, 10);
    }


    public int getSignalNumber() {
        ArrayList<AprilTagDetection> currentDetections = pipeline.getLatestDetections();
        for (AprilTagDetection tag : currentDetections) {
            if (tag.id == Left || tag.id == Middle || tag.id == Right) {
                tagOfInterest = tag;
                break;
            }
        }
        if (tagOfInterest != null) {
            return tagOfInterest.id;
        }
        //return left by default
        return Left;
    }

    public void stop() {
        try {
            webcam.stopStreaming();
        }
        catch (Throwable e)
        {}
    }
}