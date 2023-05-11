package org.firstinspires.ftc.teamcode.robot.components.vision.detector;

import org.firstinspires.ftc.teamcode.robot.components.vision.ObjectDetectorWebcam;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A class to detect objects on the field
 *
 * For this year the objects we try to detect are red and blue cones and yellow poles
 */
public class ObjectDetector {

    public enum ObjectType {
            RedCone, BlueCone, Pole, BlueTape, RedTape
    }

    // Detectable objects
    private final HashMap<ObjectType, DetectableObject> detectableObjects = new HashMap<>();
    {
        HsvBounds[] redConeBounds = {
               new HsvBounds(new Scalar(0, 50, 70), new Scalar(10, 210, 255)),
               new HsvBounds(new Scalar(170, 50, 70), new Scalar(180, 210, 255)),
        };
        this.addObject(new DetectableObject(ObjectType.RedCone, redConeBounds, 4, 5));

        HsvBounds[] redTapeBounds = {
                new HsvBounds(new Scalar(0, 211, 70), new Scalar(10, 255, 255)),
                //new HsvBounds(new Scalar(170, 211, 70), new Scalar(180, 255, 255))
        };
        this.addObject(new DetectableObject(ObjectType.RedTape, redTapeBounds, 4, 5));

        HsvBounds[] blueTapeBounds = {
                new HsvBounds(new Scalar(120, 50, 70), new Scalar(155, 255, 200))
        };
        this.addObject(new DetectableObject(ObjectType.BlueTape, blueTapeBounds, 4, 5));

        HsvBounds[] blueConeBounds = {
                new HsvBounds(new Scalar(161, 50, 70), new Scalar(170, 255, 200))
        };
        this.addObject(new DetectableObject(ObjectType.BlueCone, blueConeBounds, 4, 5));

        HsvBounds[] yellowPoleBounds = {
                new HsvBounds(new Scalar(18, 50, 70), new Scalar(45, 255, 255))
        };
        this.addObject(new DetectableObject(ObjectType.Pole, yellowPoleBounds, 1, 10));
    }

    Rect areaOfInterest;

    int minAllowedX;
    int maxAllowedX;
    int minAllowedY;
    int maxAllowedY;
    double minArea;

    // Cache
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat mMask = new Mat();
    Mat mSingularMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();
    Mat nothingMat = new Mat();
    List<MatOfPoint> objectsFound = new ArrayList<>();

    public ObjectDetector(int minAllowedX, int maxAllowedX, int minAllowedY, int maxAllowedY,
                          double minArea) {
        this.minAllowedX = minAllowedX;
        this.maxAllowedX = maxAllowedX;
        this.minAllowedY = minAllowedY;
        this.maxAllowedY = maxAllowedY;
        this.minArea = minArea;
        setupAreaOfInterest();
    }

    public void addObject(DetectableObject object) {
        this.detectableObjects.put(object.getType(), object);
    }

    private void setupAreaOfInterest() {
        areaOfInterest = new Rect(minAllowedX, minAllowedY, maxAllowedX-minAllowedX, maxAllowedY-minAllowedY);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),
                "Camera: %s x:%d-%d, y:%d-%d",
                getDetectionStatus(), minAllowedX, maxAllowedX, minAllowedY, maxAllowedY
                );
    }

    public String getDetectionStatus() {
        StringBuilder status = new StringBuilder();
        for (ObjectType objectType: detectableObjects.keySet()) {
            status.append(objectType).append(": ").append(Objects.requireNonNull(detectableObjects.get(objectType)).getFoundObjects().size()).append(", ");
        }
        return status.toString();
    }

    public Rect getRectangleOfInterest() {
        return areaOfInterest;
    }

    /**
     * Take an rgb image and return a map of objects detected
     * @param rgbaImage
     * @return
     */
    public Map<ObjectType, DetectableObject> process(Mat rgbaImage) {
        //pyramid down twice
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);
        //convert to HSV so we can use hsv range of objects to filter
        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        //go though each of our detectable objects
        for (ObjectType objectType : detectableObjects.keySet()) {
            mMask = Mat.zeros(ObjectDetectorWebcam.Y_PIXEL_COUNT, ObjectDetectorWebcam.X_PIXEL_COUNT, CvType.CV_8UC1);

            DetectableObject detectableObject = detectableObjects.get(objectType);
            detectableObject.clearFoundObjects();
            //go through each specified hsv bounds of the detectable object
            for (HsvBounds bounds : detectableObject.getHsvBounds()) {
                //Match.log("Looking for " + objectType + " objects hsv values " + bounds.toString());
                //mMask = Mat.zeros(ObjectDetectorWebcam.Y_PIXEL_COUNT, ObjectDetectorWebcam.X_PIXEL_COUNT, CvType.CV_8UC1);
                //remove all aspects of the image except those within the hsv bounds
                Core.inRange(mHsvMat, bounds.getLowerBound(), bounds.getUpperBound(), mSingularMask);
                Core.bitwise_or(mMask, mSingularMask, mMask);
            }

            //dilate image to get less sharp images
            Imgproc.dilate(mMask, mDilatedMask, nothingMat);
            objectsFound.clear();
            //find the contours in the dilated image
            Imgproc.findContours(mDilatedMask, objectsFound, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            //check each contour found
            for (MatOfPoint contour : objectsFound) {
                Rect boundingRectangle = Imgproc.boundingRect(contour);
                //check to see if the contour is within our specified x and y limits
                if (boundingRectangle.x*4 <= maxAllowedX && boundingRectangle.x*4 >= minAllowedX
                        && boundingRectangle.y*4 <= maxAllowedY && boundingRectangle.y*4 >= minAllowedY) {
                    double area = Imgproc.contourArea(contour);
                    //check to see if contour area is at least our minimum area
                    if (area >= minArea) {
                        //zoom into contour because we had pyrDown twice earlier
                        Core.multiply(contour, new Scalar(4, 4), contour);
                        detectableObject.addFoundObject(contour, area, mHsvMat);
                        //Match.log("Found " + objectType + " of area: " + area);
                    }
                    else {
                        //Match.log("Area " + area + " too small for " + objectType);
                    }
                }
            }

            //Match.log("Found " + detectableObject.getFoundObjects().size() + " " + objectType + " objects");
        }
        return detectableObjects;
    }

    /**
     * Returns the area of the largest object (in area) seen of the provided objectName
     * This year's valid object names are: RedCone, BlueCone and Pole
     * @param objectType
     * @return
     */
    public double getLargestArea(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.largestArea;
        }
        return 0;
    }

    public void decrementMinAllowedX() {
        this.minAllowedX = Math.max(minAllowedX - 1, 0);
        setupAreaOfInterest();
    }
    public void incrementMinAllowedX() {
        this.minAllowedX = Math.min(minAllowedX + 1, ObjectDetectorWebcam.X_PIXEL_COUNT -1);
        setupAreaOfInterest();
    }
    public void decrementMaxAllowedX() {
        this.maxAllowedX = Math.max(maxAllowedX - 1, 0);
        setupAreaOfInterest();
    }
    public void incrementMaxAllowedX() {
        this.maxAllowedX = Math.min(maxAllowedX + 1, ObjectDetectorWebcam.X_PIXEL_COUNT);
        setupAreaOfInterest();
    }

    public void decrementMinAllowedY() {
        this.minAllowedY = Math.max(minAllowedY - 1, 0);
        setupAreaOfInterest();
    }
    public void incrementMinAllowedY() {
        this.minAllowedY = Math.min(minAllowedY + 1, ObjectDetectorWebcam.Y_PIXEL_COUNT-1);
        setupAreaOfInterest();
    }
    public void decrementMaxAllowedY() {
        this.maxAllowedY = Math.max(maxAllowedY - 1, 0);
        setupAreaOfInterest();
    }
    public void incrementMaxAllowedY() {
        this.maxAllowedY = Math.min(maxAllowedY + 1, ObjectDetectorWebcam.Y_PIXEL_COUNT);
        setupAreaOfInterest();
    }

    /** Return the distance to the object from the camera
     *
     * @return
     */
    public double getDistanceFromCameraOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            Rect boundingRectangle = Imgproc.boundingRect(detectableObject.getLargestObject());
            return detectableObject.getWidth() * ObjectDetectorWebcam.FOCAL_LENGTH / boundingRectangle.height;
        }
        else {
            return -1;
        }
    }
    /** Returns the x position of the largest object of the type seen
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     *
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getXPositionOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getXPositionOfLargestObject();
        }
        return -1;
    }
    /** Returns the y position of the largest object of the type seen (in inches)
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     *
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getYPositionOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getYPositionOfLargestObject();
        }
        return -1;
    }

    public double getWidthOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getWidthOfLargestObject();
        }
        return -1;
    }

    public double getHeightOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getHeightOfLargestObject();
        }
        return -1;
    }

    public static class HsvBounds {
        Scalar lowerBound, upperBound;

        public HsvBounds(Scalar lowerBound, Scalar upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public Scalar getLowerBound() {
            return lowerBound;
        }

        public void setLowerBound(Scalar lowerBound) {
            this.lowerBound = lowerBound;
        }

        public Scalar getUpperBound() {
            return upperBound;
        }

        public void setUpperBound(Scalar upperBound) {
            this.upperBound = upperBound;
        }

        public String toString() {
            return String.format(Locale.getDefault(), "%s-%s",
                    lowerBound.toString(), upperBound.toString());
        }

    }

    /**
     * A class representing types of objects on the competition field
     * Currently the object differentiation is done based on HSV ranges
     *
     * The class contains a list of object of the type found
     */
    public static class DetectableObject {
        ObjectType type;
        HsvBounds[] hsvBounds;
        List<MatOfPoint> foundObjects = new ArrayList<>();
        double largestArea;
        double width;
        double height;
        int largestAreaIndex;
        Scalar largestAreaMean;

        public DetectableObject(ObjectType type, HsvBounds[] hsvBounds, double width, double height) {
            this.type = type;
            this.hsvBounds = hsvBounds;
            this.width = width;
            this.height = height;
        }

        public ObjectType getType() {
            return this.type;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public HsvBounds[] getHsvBounds() {
            return hsvBounds;
        }

        public void setHsvBounds(HsvBounds hsvBounds[]) {
            this.hsvBounds = hsvBounds;
        }

        public List<MatOfPoint> getFoundObjects() {
            return this.foundObjects;
        }
        public void clearFoundObjects() {
            this.foundObjects = new ArrayList<>();
            largestArea = 0;
            largestAreaMean = new Scalar(0, 0, 0);
        }

        /**
         * Add an object to the list of the objects of this type
         * If the area passed is greater than the largest area already found, the new object passed is
         * considered the largest object and the mean of it considered the mean of the largest object
         * @param objectFound
         * @param area - are of the object found
         */
        public void addFoundObject(MatOfPoint objectFound, double area, Mat hsv) {
            this.foundObjects.add(objectFound);
            if (area > largestArea) {
                largestAreaIndex = this.foundObjects.size() - 1;
                largestArea = area;
                largestAreaMean = Core.mean(objectFound);
            }
        }

        /**
         * Returns the largest object seen of this detectable object type
         * @return
         */
        public MatOfPoint getLargestObject() {
            if (largestAreaIndex < this.foundObjects.size()) {
                return this.foundObjects.get(largestAreaIndex);
            }
            else {
                return null;
            }
        }

        /**
         * Returns the y position of the middle of the largest object
         * @return
         */
        public int getYPositionOfLargestObject() {
            if (getLargestObject() != null) {
                Rect boundingRectangle = Imgproc.boundingRect(getLargestObject());
                return boundingRectangle.y + boundingRectangle.height / 2;
            }
            else {
                return -1;
            }
        }

        /**
         * Returns the x position of the center of the largest object
         * @return
         */
        public int getXPositionOfLargestObject() {
            if (getLargestObject() != null) {
                Rect boundingRectangle = Imgproc.boundingRect(getLargestObject());
                return boundingRectangle.x + boundingRectangle.width / 2;
            }
            else {
                return -1;
            }
        }

        /**
         * Returns the width of the largest object
         * @return
         */
        public double getWidthOfLargestObject() {
            RotatedRect rotatedRectangle = getRotatedRectangleOfLargestObject();
            if (rotatedRectangle != null) {
                return rotatedRectangle.size.height;
            }
            else {
                return -1;
            }
        }

        /**
         * Returns the height of the largest object
         * @return
         */
        public double getHeightOfLargestObject() {
            RotatedRect rotatedRectangle = getRotatedRectangleOfLargestObject();
            if (rotatedRectangle != null) {
                return rotatedRectangle.size.width;
            }
            else {
                return -1;
            }
        }

        /**
         * Return the bounding rectangle that fits the largest object
         * The rectangle is always aligned with the x and y axes
         * @return
         */
        public Rect getBoundingRectangleOfLargestObject() {
            if (getLargestObject() != null) {
                Rect boundingRectangle = Imgproc.boundingRect(getLargestObject());
                return boundingRectangle;
            }
            else {
                return null;
            }
        }

        /**
         * Return the rectangle that fits the largest object
         * The rectangle could be at an angle to the x and y axes
         * @return
         */
        public RotatedRect getRotatedRectangleOfLargestObject() {
            if (getLargestObject() != null) {
                RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(getLargestObject().toArray()));
                return rotatedRect;
            }
            else {
                return null;
            }
        }

        /**
         * Return the mean hsv values of the largest object
         * @return
         */
        public Scalar getMeanOfLargestObject() {
            if (getLargestObject() != null) {
                return largestAreaMean;
            }
            else {
                return new Scalar(0,0, 0);
            }
        }
    }
}
