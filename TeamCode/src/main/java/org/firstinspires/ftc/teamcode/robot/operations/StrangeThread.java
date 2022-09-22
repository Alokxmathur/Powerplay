package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StrangeThread extends Thread {
    boolean stop = false;
    Telemetry telemetry;
    public StrangeThread(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void run() {
        while (!stop) {
            telemetry.addData("Thread", "Thread is running");
            telemetry.update();
            Thread.yield();
        }
        telemetry.addData("Thread", "Thread is stopped");
        telemetry.update();
    }

    public void abort() {
        stop = true;
    }
}