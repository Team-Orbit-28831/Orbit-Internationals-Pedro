package pedroPathing.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.pedropathing.util.Drawing;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.commands.ClawClose;
import pedroPathing.commands.ClawUp;
import pedroPathing.commands.PivotReset;
import pedroPathing.commands.PivotResetEncoder;
import pedroPathing.commands.SlideRetract;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Sample Auto Main", group = "Test")
public class BlueBasketAuto extends OpMode {

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
    private final Pose startPose = new Pose(134.526, 48.591, Math.toRadians(0));

    // Single path from generated code
    private PathChain line1;

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        // Initialize constants and pose updater like LocalizationTest
        Constants.setConstants(FConstants.class, LConstants.class);
        poseUpdater = new PoseUpdater(hardwareMap, FConstants.class, LConstants.class);
        dashboardPoseTracker = new DashboardPoseTracker(poseUpdater);

        // Initialize dashboard telemetry using MultipleTelemetry
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        poseUpdater.setStartingPose(startPose);

        buildPaths();
        pathState = 0;

        // Initial drawing
        Drawing.drawRobot(poseUpdater.getPose(), "#4CAF50");
        Drawing.sendPacket();
    }

    public void buildPaths() {
        // Build the single path from your generated code
        PathBuilder builder = new PathBuilder();
        line1 = builder
                .addPath(
                        new BezierCurve(
                                new Point(134.526, 48.591, Point.CARTESIAN),
                                new Point(101.408, 50.365, Point.CARTESIAN),
                                new Point(126.457, 21.990, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-225))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Follow the single path
                follower.followPath(line1);
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
                    SequentialCommandGroup doBask = new SequentialCommandGroup(
                            new ClawUp(claw),
                            new ClawClose(claw),
                            new SlideRetract(cascadeSlides),
                            new WaitCommand(20),
                            new PivotReset(cascadePivot),
                            new WaitCommand(1300),
                            new PivotResetEncoder(cascadePivot)
                    );
                    setPathState(2);
                }
                break;
            case 2:
                // End state - path complete, keep showing completion message
                telemetryA.addData("STATUS", "FINISHED - ROBOT AT DESTINATION");
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
        // Update pose and dashboard tracking like LocalizationTest
        poseUpdater.update();
        dashboardPoseTracker.update();

        // Update Pedro Pathing
        follower.update();

        // Run autonomous state machine
        autonomousPathUpdate();
        // do commands n stuff
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
        telemetryA.addData("Target X", 126.457);
        telemetryA.addData("Target Y", 21.990);
        telemetryA.addData("Target Heading (deg)", -225);
        telemetryA.update();

        // Drawing for dashboard visualization (same as LocalizationTest)
        Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
        Drawing.drawRobot(currentPose, "#4CAF50");
        Drawing.sendPacket();
    }

    @Override
    public void init_loop() {
        telemetryA.addData("Status", "Initialized - Ready to start");
        telemetryA.addData("Starting X", startPose.getX());
        telemetryA.addData("Starting Y", startPose.getY());
        telemetryA.addData("Starting Heading", Math.toDegrees(startPose.getHeading()));
        telemetryA.update();
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