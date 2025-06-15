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

@Autonomous(name = "auto test 2 - slower", group = "Autonomous")
public class autonomous_test_2 extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    // Starting pose
    private final Pose startPose = new Pose(133.604, 71.807, Math.toRadians(270));

    // Path chain from visualizer
    private PathChain generatedPath;

    @Override
    public void init() {
        // Setup constants and follower
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        buildPaths();
        pathState = 0;
    }

    public void buildPaths() {
        generatedPath = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(133.604, 71.807, Point.CARTESIAN),
                                new Point(127.059, 25.027, Point.CARTESIAN),
                                new Point(84.898, 25.604, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setPathEndVelocityConstraint(10) // Slow down first segment to 20 in/sec
                .addPath(
                        new BezierLine(
                                new Point(84.898, 25.604, Point.CARTESIAN),
                                new Point(126.866, 26.759, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .setPathEndVelocityConstraint(10) // Even slower for middle segment
                .addPath(
                        new BezierCurve(
                                new Point(126.866, 26.759, Point.CARTESIAN),
                                new Point(84.321, 23.487, Point.CARTESIAN),
                                new Point(85.668, 17.134, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setPathEndVelocityConstraint(10) // Slowest for final approach
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(generatedPath);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    setPathState(-1);
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
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Max Velocity", "20/15/12 in/sec"); // Added for reference
        telemetry.update();
    }
}