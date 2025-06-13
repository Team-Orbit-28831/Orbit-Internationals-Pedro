package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "PedroPathing_test", group = "Autonomous")
public class autonomous_test extends OpMode {

    // Required variables
    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    // Starting pose
    private final Pose startPose = new Pose(9.757, 84.983, Math.toRadians(270));

    // Path variables
    private PathChain generatedPath;

    @Override
    public void init() {
        // Initialize timer and constants
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        // Initialize follower with constants classes
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        // Build paths
        buildPaths();

        // Initialize path state
        pathState = 0;
    }

    public void buildPaths() {
        // Build the complete path chain based on your original path
        generatedPath = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(9.757, 84.983, Point.CARTESIAN),
                                new Point(9.241, 59.679, Point.CARTESIAN),
                                new Point(72.193, 24.064, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                new Point(72.193, 24.064, Point.CARTESIAN),
                                new Point(11.743, 25.027, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .setReversed(true)
                .addPath(
                        new BezierLine(
                                new Point(11.743, 25.027, Point.CARTESIAN),
                                new Point(72.385, 24.257, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                new Point(72.385, 24.257, Point.CARTESIAN),
                                new Point(72.193, 13.091, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Point(72.193, 13.091, Point.CARTESIAN),
                                new Point(10.396, 13.091, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .setReversed(true)
                .addPath(
                        new BezierLine(
                                new Point(10.396, 13.091, Point.CARTESIAN),
                                new Point(72.000, 13.091, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                new Point(72.000, 13.091, Point.CARTESIAN),
                                new Point(72.000, 8.856, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Point(72.000, 8.856, Point.CARTESIAN),
                                new Point(10.203, 9.048, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .setReversed(true)
                .addPath(
                        new BezierLine(
                                new Point(10.203, 9.048, Point.CARTESIAN),
                                new Point(9.818, 84.706, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start following the generated path
                follower.followPath(generatedPath);
                setPathState(1);
                break;

            case 1: // Wait until path is completed
                if (!follower.isBusy()) {
                    setPathState(-1); // End the autonomous routine
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        // Update follower
        follower.update();

        // Update autonomous path logic
        autonomousPathUpdate();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("Position", follower.getPose().toString());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.update();
    }
}