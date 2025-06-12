package pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

public class autonomous_test {
    public class GeneratedPath {

        public GeneratedPath() {
            PathBuilder builder = new PathBuilder();

            builder
                    .addPath(
                            // Line 1
                            new BezierCurve(
                                    new Point(9.757, 84.983, Point.CARTESIAN),
                                    new Point(9.241, 59.679, Point.CARTESIAN),
                                    new Point(72.193, 24.064, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            // Line 2
                            new BezierLine(
                                    new Point(72.193, 24.064, Point.CARTESIAN),
                                    new Point(11.743, 25.027, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .setReversed(true)
                    .addPath(
                            // Line 3
                            new BezierLine(
                                    new Point(11.743, 25.027, Point.CARTESIAN),
                                    new Point(72.385, 24.257, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            // Line 4
                            new BezierLine(
                                    new Point(72.385, 24.257, Point.CARTESIAN),
                                    new Point(72.193, 13.091, Point.CARTESIAN)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .addPath(
                            // Line 5
                            new BezierLine(
                                    new Point(72.193, 13.091, Point.CARTESIAN),
                                    new Point(10.396, 13.091, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .setReversed(true)
                    .addPath(
                            // Line 6
                            new BezierLine(
                                    new Point(10.396, 13.091, Point.CARTESIAN),
                                    new Point(72.000, 13.091, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            // Line 7
                            new BezierLine(
                                    new Point(72.000, 13.091, Point.CARTESIAN),
                                    new Point(72.000, 8.856, Point.CARTESIAN)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .addPath(
                            // Line 8
                            new BezierLine(
                                    new Point(72.000, 8.856, Point.CARTESIAN),
                                    new Point(10.203, 9.048, Point.CARTESIAN)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .setReversed(true)
                    .addPath(
                            // Line 9
                            new BezierLine(
                                    new Point(10.203, 9.048, Point.CARTESIAN),
                                    new Point(9.818, 84.706, Point.CARTESIAN)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0));
        }
    }


}
