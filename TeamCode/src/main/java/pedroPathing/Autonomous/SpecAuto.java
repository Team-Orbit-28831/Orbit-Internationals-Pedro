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
import pedroPathing.commands.PivotSpecDrop;
import pedroPathing.commands.PivotZero;
import pedroPathing.commands.SlideRetract;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.commands.SlidesSampleShort;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "SpecAuto", group = "Test")
public class SpecAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;
    private PoseUpdater poseUpdater;
    private DashboardPoseTracker dashboardPoseTracker;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;

    // Dashboard telemetry - using MultipleTelemetry like LocalizationTest
    private Telemetry telemetryA;

    // Starting pose - matches your generated path start point
    private final Pose startPose = new Pose(9.346153846153847, 63.173076923076934, Math.toRadians(0));

    // Path from generated code
    private PathChain gotoplacespec;
    private ElapsedTime clock;

    @Override
    public void init() {
        try {
            pathTimer = new Timer();
            opmodeTimer = new Timer();
            opmodeTimer.resetTimer();

            // Initialize subsystems here instead of constructor
            cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
            cascadePivot = new CascadePivot(hardwareMap, telemetry);
            claw = new Claw();
            claw.init(hardwareMap); // Don't forget to initialize the claw

            // Initialize constants and pose updater like LocalizationTest
            Constants.setConstants(FConstants.class, LConstants.class);
            poseUpdater = new PoseUpdater(hardwareMap);
            dashboardPoseTracker = new DashboardPoseTracker(poseUpdater);

            // Initialize dashboard telemetry using MultipleTelemetry
            telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

            follower = new Follower(hardwareMap);
            follower.setStartingPose(startPose);
            poseUpdater.setStartingPose(startPose);

            buildPaths();
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
        // Build the path from your generated code
        PathBuilder builder = new PathBuilder();
        gotoplacespec = builder
                .addPath(
                        // Line 1
                        new BezierCurve(
                                new Point(9.346, 63.173, Point.CARTESIAN),

                                new Point(30.077, 73.385, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))

                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Start following the generated path with initial subsystem setup
                CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                        new ClawClose(claw),
                        new ClawUp(claw),
                        new PivotSpecDrop(cascadePivot)
                ));
                follower.setMaxPower(1);
                follower.followPath(gotoplacespec);
                setPathState(1);
                break;

            case 1:
                // Wait for path to complete
                if(!follower.isBusy()) {
                    telemetryA.addData("STATUS", "PATH COMPLETE!");
                    telemetryA.addData("Final X", follower.getPose().getX());
                    telemetryA.addData("Final Y", follower.getPose().getY());
                    telemetryA.addData("Final Heading", Math.toDegrees(follower.getPose().getHeading()));
                    telemetryA.addData("Total Time", opmodeTimer.getElapsedTimeSeconds());

                    // Execute end-of-path sequence

                    setPathState(2);
                }
                break;

            case 2:
                // Final state - autonomous complete
                telemetryA.addData("STATUS", "AUTONOMOUS COMPLETE");
                break;
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
            // Update pose and dashboard tracking like LocalizationTest
            poseUpdater.update();
            dashboardPoseTracker.update();

            // Update Pedro Pathing

            follower.update();

            // Run autonomous state machine
            autonomousPathUpdate();

            // Run command scheduler
            CommandScheduler.getInstance().run();

            // Get current pose from pose updater (consistent with LocalizationTest)
            Pose currentPose = poseUpdater.getPose();

            // Telemetry to both Driver Station and Dashboard using MultipleTelemetry
            telemetryA.addData("=== PATH INFO ===", "");
            telemetryA.addData("Path State", pathState);
            telemetryA.addData("Is Busy", follower.isBusy());
            telemetryA.addData("Path Timer", pathTimer.getElapsedTimeSeconds());

            telemetryA.addData("=== ROBOT POSE ===", "");
            telemetryA.addData("x", currentPose.getX());
            telemetryA.addData("y", currentPose.getY());
            telemetryA.addData("heading", currentPose.getHeading());
            telemetryA.addData("total heading", poseUpdater.getTotalHeading());

            telemetryA.addData("=== TARGET ===", "");
            telemetryA.addData("Target X", 45.346);
            telemetryA.addData("Target Y", 72.865);
            telemetryA.addData("Target Heading (deg)", 0);
            telemetryA.update();

            // Drawing for dashboard visualization (same as LocalizationTest)
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
        telemetryA.addData("Status", "STARTED - Following path...");
    }

    @Override
    public void stop() {
        telemetryA.addData("Status", "STOPPED");
    }
}