package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * Simple Pedro Pathing autonomous that follows a sequence of generated paths.
 * The robot will follow line1 -> line2 -> line3 -> line4 in sequence.
 */

@Autonomous(name = "Simple Pedro Auto", group = "Pedro")
public class autonomous_test extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    // Generated paths
    private PathBuilder builder;
    private PathChain line1, line2, line3, line4;

    /** Start pose - using the first point of line1 */
    private final Pose startPose = new Pose(9.519, 55.038, Math.toRadians(0));

    /** Build all the paths */
    public void buildPaths() {
        builder = new PathBuilder();

        line1 = builder
                .addPath(
                        new BezierLine(
                                new Point(9.519, 55.038, Point.CARTESIAN),
                                new Point(35.481, 69.231, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line2 = builder
                .addPath(
                        new BezierCurve(
                                new Point(35.481, 69.231, Point.CARTESIAN),
                                new Point(6.404, 29.942, Point.CARTESIAN),
                                new Point(30.981, 32.712, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line3 = builder
                .addPath(
                        new BezierCurve(
                                new Point(30.981, 32.712, Point.CARTESIAN),
                                new Point(81.000, 35.135, Point.CARTESIAN),
                                new Point(73.558, 21.115, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        line4 = builder
                .addPath(
                        new BezierLine(
                                new Point(73.558, 21.115, Point.CARTESIAN),
                                new Point(18.000, 21.288, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();
    }

    /** Main path following state machine */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Follow line1
                follower.followPath(line1);
                setPathState(1);
                break;

            case 1:
                // Wait for line1 to complete, then follow line2
                if (!follower.isBusy()) {
                    follower.followPath(line2, true);
                    setPathState(2);
                }
                break;

            case 2:
                // Wait for line2 to complete, then follow line3
                if (!follower.isBusy()) {
                    follower.followPath(line3, true);
                    setPathState(3);
                }
                break;

            case 3:
                // Wait for line3 to complete, then follow line4
                if (!follower.isBusy()) {
                    follower.followPath(line4, true);
                    setPathState(4);
                }
                break;

            case 4:
                // All paths complete
                if (!follower.isBusy()) {
                    setPathState(5);
                }
                break;

            case 5:
                // Autonomous complete - do nothing
                break;
        }
    }

    /** Set the path state and reset timer */
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** Main loop - runs repeatedly after clicking "Play" */
    @Override
    public void loop() {
        // Update follower and path state machine
        follower.update();
        autonomousPathUpdate();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (degrees)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.update();
    }

    /** Initialize the OpMode */
    @Override
    public void init() {
        pathTimer = new Timer();

        // Initialize Pedro Pathing
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        // Build all paths
        buildPaths();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    /** Called continuously while waiting for start */
    @Override
    public void init_loop() {
        telemetry.addData("Status", "Ready to start");
        telemetry.addData("Starting Pose", "(" + startPose.getX() + ", " + startPose.getY() + ")");
        telemetry.update();
    }

    /** Called once when start is pressed */
    @Override
    public void start() {
        pathTimer.resetTimer();
        setPathState(0);
        telemetry.addData("Status", "Started!");
        telemetry.update();
    }

    /** Called once when stop is pressed */
    @Override
    public void stop() {
        // Everything should automatically disable
    }
}