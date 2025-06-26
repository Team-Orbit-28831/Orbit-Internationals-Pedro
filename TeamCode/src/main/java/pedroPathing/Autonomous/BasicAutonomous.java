package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
// import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Internationals Autonomous", group = "Autonomous")
public class BasicAutonomous extends OpMode {

    // Static path definitions (like GeneratedPaths structure)
    public static PathBuilder builder = new PathBuilder();

    public static PathChain line1 = builder
            .addPath(
                    new BezierLine(
                            new Point(8.471, 75.080, Point.CARTESIAN),
                            new Point(37.733, 75.273, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    public static PathChain line2 = builder
            .addPath(
                    new BezierCurve(
                            new Point(37.733, 75.273, Point.CARTESIAN),
                            new Point(29.070, 47.358, Point.CARTESIAN),
                            new Point(65.840, 23.679, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    public static PathChain line3 = builder
            .addPath(
                    new BezierLine(
                            new Point(65.840, 23.679, Point.CARTESIAN),
                            new Point(11.358, 23.679, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .build();

    public static PathChain line4 = builder
            .addPath(
                    new BezierCurve(
                            new Point(11.358, 23.679, Point.CARTESIAN),
                            new Point(60.257, 25.219, Point.CARTESIAN),
                            new Point(63.144, 15.016, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    public static PathChain line5 = builder
            .addPath(
                    new BezierLine(
                            new Point(63.144, 15.016, Point.CARTESIAN),
                            new Point(12.128, 14.631, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .build();

    public static PathChain line6 = builder
            .addPath(
                    new BezierCurve(
                            new Point(12.128, 14.631, Point.CARTESIAN),
                            new Point(59.102, 17.519, Point.CARTESIAN),
                            new Point(62.182, 8.086, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    public static PathChain line7 = builder
            .addPath(
                    new BezierLine(
                            new Point(62.182, 8.086, Point.CARTESIAN),
                            new Point(13.668, 8.663, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .build();

    public static PathChain line8 = builder
            .addPath(
                    new BezierLine(
                            new Point(13.668, 8.663, Point.CARTESIAN),
                            new Point(13.668, 45.048, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    // Single complex path combining all segments
    public static PathChain complexPath = builder
            .addPath(
                    new BezierLine(
                            new Point(8.471, 75.080, Point.CARTESIAN),
                            new Point(37.733, 75.273, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .addPath(
                    new BezierCurve(
                            new Point(37.733, 75.273, Point.CARTESIAN),
                            new Point(29.070, 47.358, Point.CARTESIAN),
                            new Point(65.840, 23.679, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .addPath(
                    new BezierLine(
                            new Point(65.840, 23.679, Point.CARTESIAN),
                            new Point(11.358, 23.679, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .addPath(
                    new BezierCurve(
                            new Point(11.358, 23.679, Point.CARTESIAN),
                            new Point(60.257, 25.219, Point.CARTESIAN),
                            new Point(63.144, 15.016, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .addPath(
                    new BezierLine(
                            new Point(63.144, 15.016, Point.CARTESIAN),
                            new Point(12.128, 14.631, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .addPath(
                    new BezierCurve(
                            new Point(12.128, 14.631, Point.CARTESIAN),
                            new Point(59.102, 17.519, Point.CARTESIAN),
                            new Point(62.182, 8.086, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .addPath(
                    new BezierLine(
                            new Point(62.182, 8.086, Point.CARTESIAN),
                            new Point(13.668, 8.663, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .setReversed(true)
            .addPath(
                    new BezierLine(
                            new Point(13.668, 8.663, Point.CARTESIAN),
                            new Point(13.668, 45.048, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    // Required variables
    private Follower follower;
    private Timer pathTimer, actionTimer;
    private int pathState;
    private int currentPathIndex;

    // Starting pose
    private final Pose startPose = new Pose(8.471, 75.080, Math.toRadians(0));

    // Path variables
    private List<PathChain> pathSequence;
    private PathChain singleComplexPath;

    // Configuration - choose which approach to use
    final private boolean useSequentialPaths = true; // Set to false to use single complex path

    @Override
    public void init() {
        // Initialize timers and constants
        pathTimer = new Timer();
        actionTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        // Initialize follower with constants classes
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        // Build paths based on configuration
        if (useSequentialPaths) {
            buildSequentialPaths();
        } else {
            buildSingleComplexPath();
        }

        // Initialize path state
        pathState = 0;
        currentPathIndex = 0;
    }

    public void buildSequentialPaths() {
        pathSequence = new ArrayList<>();

        // Add all individual paths (now defined in this same class)
        pathSequence.add(line1);
        pathSequence.add(line2);
        pathSequence.add(line3);
        pathSequence.add(line4);
        pathSequence.add(line5);
        pathSequence.add(line6);
        pathSequence.add(line7);
        pathSequence.add(line8);
    }

    public void buildSingleComplexPath() {
        // Use the pre-built complex path (now defined in this same class)
        singleComplexPath = complexPath;
    }

    public void autonomousPathUpdate() {
        if (useSequentialPaths) {
            sequentialPathLogic();
        } else {
            singlePathLogic();
        }
    }

    private void sequentialPathLogic() {
        switch (pathState) {
            case 0: // Start next path in sequence
                if (currentPathIndex < pathSequence.size()) {
                    follower.followPath(pathSequence.get(currentPathIndex));
                    setPathState(1);
                } else {
                    setPathState(-1); // All paths completed
                }
                break;

            case 1: // Wait until current path is completed
                if (!follower.isBusy()) {
                    // Path completed, add any actions here if needed
                    performPathCompletionAction();
                    currentPathIndex++;
                    setPathState(0); // Move to next path
                }
                break;

            case 2: // Optional
                if (actionTimer.getElapsedTimeSeconds() > 1.0) { // 1 second action
                    setPathState(0); // Return to path following
                }
                break;
        }
    }

    private void singlePathLogic() {
        switch (pathState) {
            case 0: // Start following the single complex path
                follower.followPath(singleComplexPath);
                setPathState(1);
                break;

            case 1: // Wait until path is completed
                if (!follower.isBusy()) {
                    setPathState(-1); // End the autonomous routine
                }
                break;
        }
    }

    private void performPathCompletionAction() {
        // Add any actions needed between paths
        // For example: intake control, scoring mechanisms, etc.

        // Example of when to add a delay action:
        if (currentPathIndex == 1 || currentPathIndex == 3 || currentPathIndex == 5) {
            // After scoring paths, might want to add a brief action
            actionTimer.resetTimer();
            setPathState(2);
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
        telemetry.addData("Approach", useSequentialPaths ? "Sequential" : "Single Complex");
        telemetry.addData("Path State", pathState);
        if (useSequentialPaths) {
            telemetry.addData("Current Path", currentPathIndex + 1);
            telemetry.addData("Total Paths", pathSequence.size());
        }
        telemetry.addData("Position", follower.getPose().toString());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Path Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
    }

    @Override
    public void stop() {
        // Clean up resources
        if (follower != null) {
            follower.update(); // Final update
        }
    }
}