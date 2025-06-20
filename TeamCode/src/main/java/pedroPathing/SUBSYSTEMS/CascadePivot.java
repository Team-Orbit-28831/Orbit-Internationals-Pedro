package pedroPathing.SUBSYSTEMS;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class CascadePivot implements Subsystem {

    public DcMotorEx pivotMotorLeft;   // vertical
    public DcMotorEx pivotMotorRight;  // extension

    // PIDF coefficients
    public static double p = 0.003;
    public static double i = 0;
    public static double d = 0;
    public static double f = 0 ;

    public PIDFController pidfLeft  = new PIDFController(p, i, d, f);
    public PIDFController pidfRight  = new PIDFController(p, i, d, f);
    public static double target;

    public double maxPivot = -1800;
    private final ElapsedTime timer = new ElapsedTime();

    public CascadePivot(HardwareMap hardwareMap, Telemetry telemetry) {

        pivotMotorLeft = hardwareMap.get(DcMotorEx.class, "pivotLeft");
        pivotMotorRight = hardwareMap.get(DcMotorEx.class, "pivotRight");

        pivotMotorLeft.setDirection(DcMotorEx.Direction.FORWARD);
        pivotMotorRight.setDirection(DcMotorEx.Direction.FORWARD);

        pivotMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        pivotMotorLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotorRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        timer.reset();


        pivotMotorLeft.setPower(0);
        pivotMotorRight.setPower(0);
        this.telemetry = telemetry;
    }


    public void setPivotTarget (double target) {
        CascadePivot.target = target;
        pidfLeft.setSetPoint(target);
        pidfRight.setSetPoint(target);
        autoUpdatePivot();

//
    }
    private Telemetry telemetry;

    public void autoUpdatePivot() {
        pidfRight.setPIDF(p,i,d,f);
        pidfLeft.setPIDF(p,i,d,f);

        double leftpos = pivotMotorLeft.getCurrentPosition();
        double leftpower = pidfLeft.calculate(leftpos, target);

        double rightpos = pivotMotorRight.getCurrentPosition();
        double rightpower = pidfRight.calculate(rightpos, target);

        telemetry.addData("Target Position", target);
        telemetry.addData("Current Position Left", leftpos);
        telemetry.addData("Current Position Right", rightpos);
        telemetry.update();


        pivotMotorLeft.setPower(leftpower);
        pivotMotorRight.setPower(rightpower);

        if (getAveragePosition() >= maxPivot) {
            stop();
        }


    }



    public void stop() {
        pivotMotorLeft.setPower(0);
        pivotMotorRight.setPower(0);
    }

    public int getAveragePosition() {
        return (pivotMotorLeft.getCurrentPosition() + pivotMotorRight.getCurrentPosition()) / 2;
    }

    public void setPower(double val) {
        pivotMotorLeft.setPower(val);
        pivotMotorRight.setPower(val);
    }
    @Override
    public void periodic() {
        autoUpdatePivot();
    }
}