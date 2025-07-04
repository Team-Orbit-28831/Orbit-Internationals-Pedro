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

/**
 * This is a path-only autonomous that uses the paths from GeneratedPaths.
 * It executes all 15 path segments in sequence without any subsystem controls.
 *
 * @author Generated from GeneratedPaths
 * @version 1.0
 */

@Autonomous(name = "6 Spec Auto", group = "PathOnly")
public class Six_SpecAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    /** This is the variable where we store the state of our auto. */
    private int pathState;

    /** Start Pose - based on first point from GeneratedPaths */
    private final Pose startPose = new Pose(9.692, 55.731, Math.toRadians(0));

    // PathChains for all 15 paths from GeneratedPaths
    private PathChain line1, line2, line3, line4, line5, line6, line7, line8,
            line9, line10, line11, line12, line13, line14, line15;

    /** Build all the paths from GeneratedPaths data */
    public void buildPaths() {

        line1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(9.692, 55.731, Point.CARTESIAN),
                                new Point(37.904, 70.269, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(37.904, 70.269, Point.CARTESIAN),
                                new Point(12.462, 29.942, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180))
                .build();

        line3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(12.462, 29.942, Point.CARTESIAN),
                                new Point(38.077, 63.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line4 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(38.077, 63.692, Point.CARTESIAN),
                                new Point(23.538, 20.077, Point.CARTESIAN),
                                new Point(34.269, 33.750, Point.CARTESIAN),
                                new Point(101.942, 27.346, Point.CARTESIAN),
                                new Point(86.192, 19.385, Point.CARTESIAN),
                                new Point(29.942, 19.904, Point.CARTESIAN),
                                new Point(12.462, 20.250, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))
                .build();

        line5 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(12.462, 20.250, Point.CARTESIAN),
                                new Point(118.558, 23.712, Point.CARTESIAN),
                                new Point(68.538, 12.635, Point.CARTESIAN),
                                new Point(12.462, 9.173, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line6 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(12.462, 9.173, Point.CARTESIAN),
                                new Point(71.827, 17.654, Point.CARTESIAN),
                                new Point(82.385, 3.288, Point.CARTESIAN),
                                new Point(12.115, 6.231, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line7 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(12.115, 6.231, Point.CARTESIAN),
                                new Point(12.288, 20.942, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180))
                .build();

        line8 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(12.288, 20.942, Point.CARTESIAN),
                                new Point(39.115, 80.308, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line9 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(39.115, 80.308, Point.CARTESIAN),
                                new Point(12.462, 20.423, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line10 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(12.462, 20.423, Point.CARTESIAN),
                                new Point(38.942, 75.635, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line11 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(38.942, 75.635, Point.CARTESIAN),
                                new Point(12.288, 20.769, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line12 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(12.288, 20.769, Point.CARTESIAN),
                                new Point(37.904, 65.423, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        line13 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(37.904, 65.423, Point.CARTESIAN),
                                new Point(22.500, 14.365, Point.CARTESIAN),
                                new Point(64.212, 45.692, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(90))
                .build();

        line14 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(64.212, 45.692, Point.CARTESIAN),
                                new Point(13.500, 13.500, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                .build();

        line15 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(13.500, 13.500, Point.CARTESIAN),
                                new Point(37.558, 58.154, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();
    }

    /** This switch runs the pathing for all 15 paths in sequence */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(line1);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(line2, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(line3, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(line4, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(line5, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(line6, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(line7, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(line8, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!follower.isBusy()) {
                    follower.followPath(line9, true);
                    setPathState(9);
                }
                break;

            case 9:
                if (!follower.isBusy()) {
                    follower.followPath(line10, true);
                    setPathState(10);
                }
                break;

            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(line11, true);
                    setPathState(11);
                }
                break;

            case 11:
                if (!follower.isBusy()) {
                    follower.followPath(line12, true);
                    setPathState(12);
                }
                break;

            case 12:
                if (!follower.isBusy()) {
                    follower.followPath(line13, true);
                    setPathState(13);
                }
                break;

            case 13:
                if (!follower.isBusy()) {
                    follower.followPath(line14, true);
                    setPathState(14);
                }
                break;

            case 14:
                if (!follower.isBusy()) {
                    follower.followPath(line15, true);
                    setPathState(15);
                }
                break;

            case 15:
                // Auto complete
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
        follower.setMaxPower(0.7);
        autonomousPathUpdate();

        // Simple telemetry
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {}
}