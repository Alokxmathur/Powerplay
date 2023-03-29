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

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ObjectDetectorWebcam {

    public static final double CAMERA_OFFSET_FRONT = 6.5;
    public static final int FOCAL_LENGTH = 1500;

    public static final int Y_PIXEL_COUNT = 1920;
    public static final int X_PIXEL_COUNT = 1080;
    public static final int MINIMUM_AREA = 4000;

    OpenCvWebcam webcam;
    Pipeline pipeline;

    public static final Object synchronizer = new Object();
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        pipeline = new Pipeline();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM_ID), cameraMonitorViewId);
        pipeline.setTelemetry(telemetry);
        webcam.setPipeline(pipeline);
        start();
    }

    public void start() {
        Match.log("Attempting to start " + RobotConfig.WEBCAM_ID + " usage");

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        //webcam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.showFpsMeterOnViewport(false);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
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
            public void onError(int errorCode)
            {
                Match.log("Unable to open " + RobotConfig.WEBCAM_ID);
            }
        });
        FtcDashboard.getInstance().startCameraStream(webcam, 10);
    }

    /**
     * Return the road runner pose of the object seen relative to the robot.
     * The robot is assumed to be facing the y axis with positive being in front of the robot.
     * Positive X axis is assumed to be running away to robot's right
     * @return Pose of object relative to the robot's center
     */
    public Pose2d getRelativeObjectPosition() {
        return null;
    }

    /**
     * Return the road runner pose of the object seen relative to the field.
     *
     * @return Pose of object in relation to the field
     */
    public Pose2d getAbsoluteObjectPosition(Pose2d robotPosition) {

        Pose2d relativeObjectPosition = getRelativeObjectPosition();
        if (relativeObjectPosition != null) {
            double heading = AngleUnit.normalizeRadians
                    (robotPosition.getHeading() + Math.toRadians(270) + relativeObjectPosition.getHeading());
            double hypotenuse = Math.hypot(relativeObjectPosition.getX(), relativeObjectPosition.getY());
            double x = Math.cos(heading) * hypotenuse;
            double y = Math.sin(heading) * hypotenuse;
            Vector2d objectAbsoluteVector = robotPosition.vec().plus(new Vector2d(x, y));
            return new Pose2d(objectAbsoluteVector, heading);
        }
        return null;
    }

    public void decrementMinX() {
        pipeline.objectDetector.decrementMinAllowedX();
    }

    public void incrementMinX() {
        pipeline.objectDetector.incrementMinAllowedX();
    }
    public void decrementMaxX() {
        pipeline.objectDetector.decrementMaxAllowedX();
    }
    public void incrementMaxX() {
        pipeline.objectDetector.incrementMaxAllowedX();
    }
    public void decrementMinY() {
        pipeline.objectDetector.decrementMinAllowedY();
    }
    public void incrementMinY() {
        pipeline.objectDetector.incrementMinAllowedY();
    }
    public void decrementMaxY() {
        pipeline.objectDetector.decrementMaxAllowedY();
    }
    public void incrementMaxY() {
        pipeline.objectDetector.incrementMaxAllowedY();
    }

    public double getXPositionOfLargestObject(ObjectDetector.ObjectType objectType) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getXPositionOfLargestObject(objectType);
        }
    }
    public double getYPositionOfLargestObject(ObjectDetector.ObjectType objectType) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getYPositionOfLargestObject(objectType);
        }
    }

    public double getWidthOfLargestObject(ObjectDetector.ObjectType objectType) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getWidthOfLargestObject(objectType);
        }
    }

    public double getHeightOfLargestObject(ObjectDetector.ObjectType objectType) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getHeightOfLargestObject(objectType);
        }
    }

    public boolean seeingObject(ObjectDetector.ObjectType objectName) {
        synchronized (synchronizer) {
            return getLargestArea(objectName) > 0;
        }
    }

    public double getLargestArea(ObjectDetector.ObjectType objectName) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getLargestArea(objectName);
        }
    }

    public double getDistanceToLargestObject(ObjectDetector.ObjectType objectType) {
        synchronized (synchronizer) {
            return pipeline.objectDetector.getDistanceFromCameraOfLargestObject(objectType);
        }
    }

    public class Pipeline extends OpenCvPipeline {
        Telemetry telemetry;
        public void setTelemetry(Telemetry telemetry) {
            this.telemetry = telemetry;
        }

        /*
         * Red, Blue and Green color constants
         */
        final Scalar RED = new Scalar(255, 0, 0);
        final Scalar GREEN = new Scalar(0, 255, 0);
        final Scalar SILVER = new Scalar(192, 192, 192);

        ObjectDetector objectDetector = new ObjectDetector(
                0, X_PIXEL_COUNT, 0, Y_PIXEL_COUNT, MINIMUM_AREA);

        @Override
        public Mat processFrame(Mat input) {
            synchronized (synchronizer) {
                Map<ObjectDetector.ObjectType, ObjectDetector.DetectableObject> detectedObjects = objectDetector.process(input);
                for (ObjectDetector.ObjectType objectType: detectedObjects.keySet()) {
                    ObjectDetector.DetectableObject detectableObject = detectedObjects.get(objectType);
                    //Imgproc.drawContours(input, detectableObject.foundObjects, -1, SILVER, 2);
                    MatOfPoint largestContour = detectableObject.getLargestObject();
                    if (largestContour != null) {
                        List<MatOfPoint> largestContours = new ArrayList<>();
                        largestContours.add(detectableObject.getLargestObject());
                        Imgproc.drawContours(input, largestContours, -1, GREEN, 2);
                        RotatedRect rotatedRectangle = detectableObject.getRotatedRectangleOfLargestObject();
                        if (rotatedRectangle != null) {
                            Point[] vertices = new Point[4];
                            rotatedRectangle.points(vertices);
                            MatOfPoint points = new MatOfPoint(vertices);
                            Imgproc.drawContours(input, Arrays.asList(points), -1, RED, 1);
                        }
                        Point point = new Point(detectableObject.getXPositionOfLargestObject(), detectableObject.getYPositionOfLargestObject());
                        double distance = objectDetector.getDistanceFromCameraOfLargestObject(objectType);
                        /*
                        Imgproc.putText(input,
                                String.format(Locale.getDefault(), "%s %.2f\" away",
                                        objectType, distance),
                                point,
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                1,
                                SILVER,
                                2
                                );
                         */
                        Scalar mean = detectableObject.getMeanOfLargestObject();
                        Imgproc.putText(input,
                                String.format(Locale.getDefault(), "%s %.0f,%.0f,%.0f",
                                        objectType, mean.val[0], mean.val[1], mean.val[2]),
                                point,
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                1,
                                SILVER,
                                2
                        );
                    }
                }
                Rect limitsRectangle = objectDetector.getRectangleOfInterest();
                Imgproc.rectangle(input, limitsRectangle, RED, 2);
                Thread.yield();

                return input;
            }
        }
    }

    public String getStatus() {
        return pipeline.objectDetector.getStatus();

    }
     public void stop() {
        //webcam.stopStreaming();
     }
}