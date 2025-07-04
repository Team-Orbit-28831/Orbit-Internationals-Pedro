package pedroPathing.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.pedropathing.util.Drawing;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.commands.ClawClose;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawFlat;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.ClawUp;
import pedroPathing.commands.PivotBask;
import pedroPathing.commands.PivotNormal;
import pedroPathing.commands.PivotReset;
import pedroPathing.commands.PivotResetEncoder;
import pedroPathing.commands.PivotSampleShort;
import pedroPathing.commands.PivotZero;
import pedroPathing.commands.SlideRetract;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.commands.SlidesSampleShort;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "6 Sample Auto", group = "Test")
public class Six_SampleAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;
    private PoseUpdater poseUpdater;
    private DashboardPoseTracker dashboardPoseTracker;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;

    // Dashboard telemetry
    private Telemetry telemetryA;

    // Starting pose - matches first path start point
    private final Pose startPose = new Pose(9.519, 110.250, Math.toRadians(0));

    // All generated paths
    private PathChain line1, line2, line3, line4, line5, line6, line7, line8, line9;

    @Override
    public void init() {
        try {
            pathTimer = new Timer();
            opmodeTimer = new Timer();
            opmodeTimer.resetTimer();

            // Initialize subsystems
//            cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
//            cascadePivot = new CascadePivot(hardwareMap, telemetry);
//            claw = new Claw(hardwareMap);

            // Initialize constants and pose updater
            Constants.setConstants(FConstants.class, LConstants.class);
            poseUpdater = new PoseUpdater(hardwareMap);
            dashboardPoseTracker = new DashboardPoseTracker(poseUpdater);

            // Initialize dashboard telemetry
            telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

            follower = new Follower(hardwareMap);
            follower.setStartingPose(startPose);
            poseUpdater.setStartingPose(startPose);

            buildPaths();

            // Initial command setup
            CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new ClawClose(claw)));
            CommandScheduler.getInstance().run();

            pathState = 0;

            // Initial drawing
            Drawing.drawRobot(poseUpdater.getPose(), "#4CAF50");
            Drawing.sendPacket();

            telemetryA.addData("Status", "Initialization Complete");
            telemetryA.update();

        } catch (Exception e) {
            telemetry.addData("INIT ERROR", e.getMessage());
            telemetry.update();
        }
    }

    public void buildPaths() {
        PathBuilder builder = new PathBuilder();

        // Line 1: Starting movement
        line1 = builder
                .addPath(
                        new BezierLine(
                                new Point(9.519, 110.250, Point.CARTESIAN),
                                new Point(17.400, 126.500, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .build();

        // Line 2: First sample pickup
        line2 = builder
                .addPath(
                        new BezierLine(
                                new Point(17.400, 126.500, Point.CARTESIAN),
                                new Point(23.885, 119.769, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(0))
                .build();

        // Line 3: Back to basket
        line3 = builder
                .addPath(
                        new BezierLine(
                                new Point(23.885, 119.769, Point.CARTESIAN),
                                new Point(17.481, 126.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .build();

        // Line 4: Second sample pickup
        line4 = builder
                .addPath(
                        new BezierLine(
                                new Point(17.481, 126.692, Point.CARTESIAN),
                                new Point(23.885, 130.154, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(0))
                .build();

        // Line 5: Back to basket again
        line5 = builder
                .addPath(
                        new BezierLine(
                                new Point(23.885, 130.154, Point.CARTESIAN),
                                new Point(17.481, 126.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .build();

        // Line 6: Third sample pickup
        line6 = builder
                .addPath(
                        new BezierLine(
                                new Point(17.481, 126.692, Point.CARTESIAN),
                                new Point(23.712, 119.596, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(45))
                .build();

        // Line 7: Back to basket final time
        line7 = builder
                .addPath(
                        new BezierLine(
                                new Point(23.712, 119.596, Point.CARTESIAN),
                                new Point(17.481, 126.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(-45))
                .build();

        // Line 8: Move to observation zone
        line8 = builder
                .addPath(
                        new BezierCurve(
                                new Point(17.481, 126.692, Point.CARTESIAN),
                                new Point(76.154, 107.654, Point.CARTESIAN),
                                new Point(74.077, 94.846, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(-90))
                .build();

        // Line 9: Return from observation zone (optional)
        line9 = builder
                .addPath(
                        new BezierCurve(
                                new Point(74.077, 94.846, Point.CARTESIAN),
                                new Point(75.808, 107.481, Point.CARTESIAN),
                                new Point(17.481, 126.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-45))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Initial movement to basket area
                CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
//                        new ClawClose(claw),
//                        new ClawUp(claw),
//                        new PivotBask(cascadePivot)
                ));
                follower.followPath(line1);
//                setPathState(1);
                break;

//            case 1: // Score preload in basket
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup scorePreload = new SequentialCommandGroup(
////                            new SlidesHighBask(cascadeSlides),
////                            new WaitCommand(500),
////                            new ClawOpen(claw),
////                            new WaitCommand(300),
////                            new SlideRetract(cascadeSlides),
////                            new PivotZero(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(scorePreload);
//                    follower.followPath(line2);
//                    setPathState(2);
//                }
//                break;
//
//            case 2: // Pick up first sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup pickupSample = new SequentialCommandGroup(
////                            new PivotSampleShort(cascadePivot),
////                            new SlidesSampleShort(cascadeSlides),
////                            new ClawFlat(claw),
////                            new ClawDown(claw),
////                            new WaitCommand(300),
////                            new ClawClose(claw),
////                            new WaitCommand(200),
////                            new ClawUp(claw),
////                            new SlideRetract(cascadeSlides),
////                            new PivotBask(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(pickupSample);
//                    follower.followPath(line3);
//                    setPathState(3);
//                }
//                break;
//
//            case 3: // Score first sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup scoreSample = new SequentialCommandGroup(
////                            new SlidesHighBask(cascadeSlides),
////                            new WaitCommand(500),
////                            new ClawOpen(claw),
////                            new WaitCommand(300),
////                            new SlideRetract(cascadeSlides),
////                            new PivotZero(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(scoreSample);
//                    follower.followPath(line4);
//                    setPathState(4);
//                }
//                break;
//
//            case 4: // Pick up second sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup pickupSample2 = new SequentialCommandGroup(
////                            new PivotSampleShort(cascadePivot),
////                            new SlidesSampleShort(cascadeSlides),
////                            new ClawFlat(claw),
////                            new ClawDown(claw),
////                            new WaitCommand(300),
////                            new ClawClose(claw),
////                            new WaitCommand(200),
////                            new ClawUp(claw),
////                            new SlideRetract(cascadeSlides),
////                            new PivotBask(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(pickupSample2);
//                    follower.followPath(line5);
//                    setPathState(5);
//                }
//                break;
//
//            case 5: // Score second sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup scoreSample2 = new SequentialCommandGroup(
////                            new SlidesHighBask(cascadeSlides),
////                            new WaitCommand(500),
////                            new ClawOpen(claw),
////                            new WaitCommand(300),
////                            new SlideRetract(cascadeSlides),
////                            new PivotZero(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(scoreSample2);
//                    follower.followPath(line6);
//                    setPathState(6);
//                }
//                break;
//
//            case 6: // Pick up third sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup pickupSample3 = new SequentialCommandGroup(
////                            new PivotSampleShort(cascadePivot),
////                            new SlidesSampleShort(cascadeSlides),
////                            new ClawFlat(claw),
////                            new ClawDown(claw),
////                            new WaitCommand(300),
////                            new ClawClose(claw),
////                            new WaitCommand(200),
////                            new ClawUp(claw),
////                            new SlideRetract(cascadeSlides),
////                            new PivotBask(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(pickupSample3);
//                    follower.followPath(line7);
//                    setPathState(7);
//                }
//                break;
//
//            case 7: // Score third sample
//                if (!follower.isBusy()) {
//                    SequentialCommandGroup scoreSample3 = new SequentialCommandGroup(
////                            new SlidesHighBask(cascadeSlides),
////                            new WaitCommand(500),
////                            new ClawOpen(claw),
////                            new WaitCommand(300),
////                            new SlideRetract(cascadeSlides),
////                            new PivotZero(cascadePivot)
//                    );
//                    CommandScheduler.getInstance().schedule(scoreSample3);
//                    follower.followPath(line8);
//                    setPathState(8);
//                }
//                break;
//
//            case 8: // Move to observation zone and finish
//                if (!follower.isBusy()) {
//                    telemetryA.addData("STATUS", "AUTONOMOUS COMPLETE!");
//                    telemetryA.addData("Final X", follower.getPose().getX());
//                    telemetryA.addData("Final Y", follower.getPose().getY());
//                    telemetryA.addData("Final Heading", Math.toDegrees(follower.getPose().getHeading()));
//                    telemetryA.addData("Total Time", opmodeTimer.getElapsedTimeSeconds());
//                    setPathState(9);
//                }
//                break;
//
//            case 9: // Complete
//                // Autonomous is finished
//                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        telemetryA.addData("Switching to state", pState);
    }

    @Override
    public void loop() {
        try {
            // Update pose and dashboard tracking
            poseUpdater.update();
            dashboardPoseTracker.update();

            // Update Pedro Pathing
            follower.update();

            // Run autonomous state machine
            autonomousPathUpdate();

            // Run commands
            CommandScheduler.getInstance().run();

            // Get current pose
            Pose currentPose = poseUpdater.getPose();

            // Telemetry
            telemetryA.addData("=== PATH INFO ===", "");
            telemetryA.addData("Path State", pathState);
            telemetryA.addData("Is Busy", follower.isBusy());
            telemetryA.addData("Path Timer", pathTimer.getElapsedTimeSeconds());
            telemetryA.addData("Total Time", opmodeTimer.getElapsedTimeSeconds());

            telemetryA.addData("=== ROBOT POSE ===", "");
            telemetryA.addData("x", currentPose.getX());
            telemetryA.addData("y", currentPose.getY());
            telemetryA.addData("heading (deg)", Math.toDegrees(currentPose.getHeading()));
            telemetryA.addData("total heading", poseUpdater.getTotalHeading());
            telemetryA.update();

            // Drawing for dashboard visualization
            Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
            Drawing.drawRobot(currentPose, "#4CAF50");
            Drawing.sendPacket();

        } catch (Exception e) {
            telemetryA.addData("LOOP ERROR", e.getMessage());
            telemetryA.update();
        }
    }

    @Override
    public void init_loop() {
        if (telemetryA != null) {
            telemetryA.addData("Status", "Initialized - Ready to start");
            telemetryA.addData("Starting X", startPose.getX());
            telemetryA.addData("Starting Y", startPose.getY());
            telemetryA.addData("Starting Heading", Math.toDegrees(startPose.getHeading()));
            telemetryA.update();
        }
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
        telemetryA.addData("Status", "STARTED - Following generated paths...");
    }

    @Override
    public void stop() {
        telemetryA.addData("Status", "STOPPED");
    }
}