package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "internationalsspecauto", group = "Autonomous")
public class InternationalsSpecAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;

    // Starting pose (adjusted for odometry issues)
    private final Pose startPose = new Pose(9.173, 39.1875, Math.toRadians(0));

    // Generated paths
    private PathChain line1, line2, line3, line4;

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPaths();
        pathState = 0;
    }

    public void buildPaths() {
        // Line 1: Starting position matches startPose
        line1 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(9.173, 39.1875, Point.CARTESIAN),
                        new Point(38.250, 44.892, Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        // Line 2: Adjusted Y coordinates for odometry issues (scaled down by 0.625)
        line2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(38.250, 44.892, Point.CARTESIAN),
                        new Point(9.519, 14.603, Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        // Line 3: Adjusted Y coordinates for odometry issues (scaled down by 0.625)
        line3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(9.519, 14.603, Point.CARTESIAN),
                        new Point(38.423, 44.892, Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();
        line4 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(38.423, 44.892, Point.CARTESIAN),
                                new Point(17.481, 7.78875, Point.CARTESIAN),
                                new Point(47.846, 30.28875, Point.CARTESIAN),
                                new Point(45.769, 14.82, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180))
                .build();
        }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Follow first path
                follower.followPath(line1);
                setPathState(1);
                break;
            case 1:
                // Wait for first path to complete, then follow second path
                if (!follower.isBusy()) {
                    follower.followPath(line2, true);
                    setPathState(2);
                }
                break;
            case 2:
                // Wait for second path to complete, then follow third path
                if (!follower.isBusy()) {
                    follower.followPath(line3, true);
                    setPathState(3);
                }
                break;
            case 3:
                // All paths completed
                if (!follower.isBusy()) {
                    follower.followPath(line4,true);
                    setPathState(4);
                }
                break;
            case 4:
                // Autonomous complete - do nothing
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        telemetry.addData("Switching to state", pState);
    }

    @Override
    public void loop() {
        // Update Pedro Pathing
        follower.update();

        // Run autonomous state machine
        autonomousPathUpdate();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Path Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.addData("Opmode Timer", opmodeTimer.getElapsedTimeSeconds());

        telemetry.update();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
        // Clean shutdown
    }
}