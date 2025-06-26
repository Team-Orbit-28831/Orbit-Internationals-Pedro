package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is a path-only autonomous that showcases pure movement without subsystem controls.
 * It follows a 0+4 (Specimen + Sample) bucket auto path pattern.
 * All servo and motor subsystem controls have been removed.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Auto_Chamber_Pedro_PathOnly", group = "Examples")
public class AutoChamberPedro extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    private int pathState;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(11, 100, Math.toRadians(-180));

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup2p5, grabPickup3p5, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(new Pose(23, 102, Math.toRadians(0)))));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), new Pose(22, 102, Math.toRadians(-180)).getHeading());

        /* Here is an example for Constant Interpolation
        scorePreload.setConstantInterpolation(startPose.getHeading()); */

        /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point((new Pose(22, 122, Math.toRadians(180)))), new Point(new Pose(5, 50, Math.toRadians(180))), new Point(new Pose(68, 85, Math.toRadians(0))), new Point(new Pose(63, 75, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(27, 102, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
                .setZeroPowerAccelerationMultiplier(1)
                .build();

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(new Pose(63, 72, Math.toRadians(180))), new Point(new Pose(18, 72, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(63, 68, Math.toRadians(180)).getHeading(), new Pose(22, 68, Math.toRadians(180)).getHeading())
                .setZeroPowerAccelerationMultiplier(10)
                .build();

        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(new Pose(22, 70, Math.toRadians(180))), new Point(new Pose(67, 84, Math.toRadians(180))), new Point(new Pose(67, 70, Math.toRadians(180))), new Point(new Pose(55, 65, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
                .setPathEndVelocityConstraint(10)
                .build();

        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(new Pose(55, 68, Math.toRadians(180))), new Point(new Pose(18, 65, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(63, 68, Math.toRadians(180)).getHeading(), new Pose(22, 68, Math.toRadians(180)).getHeading())
                .setPathEndVelocityConstraint(10)
                .build();

        grabPickup2p5 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(new Pose(22, 72, Math.toRadians(180))), new Point(new Pose(67, 60, Math.toRadians(180))), new Point(new Pose(67, 70, Math.toRadians(180))), new Point(new Pose(63, 60, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
                .setPathEndVelocityConstraint(10)
                .build();

        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(new Pose(63, 60, Math.toRadians(180))), new Point(new Pose(18, 62, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(20, 43, Math.toRadians(180)).getHeading(), new Pose(41, 80, Math.toRadians(180)).getHeading())
                .setZeroPowerAccelerationMultiplier(10)
                .build();

        grabPickup3p5 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(new Pose(22, 72, Math.toRadians(180))), new Point(new Pose(41, 80, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(20, 43, Math.toRadians(180)).getHeading(), new Pose(41, 80, Math.toRadians(180)).getHeading())
                .setPathEndVelocityConstraint(10)
                .build();

        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(new Pose(follower.getPose().getX(), follower.getPose().getY(), Math.toRadians(180))), new Point(new Pose(8, 68, Math.toRadians(180)))))
                .setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading())
                .setZeroPowerAccelerationMultiplier(1)
                .build();

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
        park = new Path(new BezierCurve(new Point(new Pose(10, 80, Math.toRadians(180))), new Point(new Pose(10, 130, Math.toRadians(180))), /* Control Point */ new Point(new Pose(30, 130, Math.toRadians(180)))));
        park.setLinearHeadingInterpolation(new Pose(63, 58, Math.toRadians(180)).getHeading(), new Pose(63, 58, Math.toRadians(180)).getHeading());
    }

    /** This switch is called continuously and runs the pathing.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup1, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup1, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup2p5, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3p5, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(9);
                }
                break;

            case 9:
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 1) {
                    follower.followPath(park, true);
                    setPathState(10);
                }
                break;

            case 10:
                // Auto complete - robot is parked
                break;
        }
    }

    /** These change the states of the paths
     * It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {
        // These loop the movements of the robot

        follower.update();
        follower.setMaxPower(0.7);
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}