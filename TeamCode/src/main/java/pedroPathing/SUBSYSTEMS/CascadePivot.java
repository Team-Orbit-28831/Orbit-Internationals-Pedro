package pedroPathing.SUBSYSTEMS;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class CascadePivot implements Subsystem {

    public DcMotorEx pivotMotorLeft;   // vertical
    public DcMotorEx pivotMotorRight;  // extension

    public int baskHeight = -3151;
    public int normalHeight = -701;

    public int collectionHeight = -481;

    public int specDepositHeight = -3099;

    public int longNormalHeight = -821;
    public int specHeight = -1691;

    public int resetHeight = 750;

    public double pivotSpeed = 0.9;


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

//        pivotMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        pivotMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

//        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        pivotMotorLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotorRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        timer.reset();

//
//        pivotMotorLeft.setPower(0);
//        pivotMotorRight.setPower(0);
        this.telemetry = telemetry;
    }


    public void setPivotTarget (double target) {
        CascadePivot.target = target;
        pidfLeft.setSetPoint(target);
        pidfRight.setSetPoint(target);
        autoUpdatePivot();
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

    }

    public void stop() {
        pivotMotorLeft.setPower(0);
        pivotMotorRight.setPower(0);
    }

    public void pivotGoToBask() {
        pivotMotorLeft.setTargetPosition(baskHeight);
        pivotMotorRight.setTargetPosition(baskHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotSpecCollect() {
        pivotMotorLeft.setTargetPosition(-831);
        pivotMotorRight.setTargetPosition(-831);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }
    public void pivotDepositSpec(){
        pivotMotorLeft.setTargetPosition(specDepositHeight);
        pivotMotorRight.setTargetPosition(specDepositHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotToCollect() {
        pivotMotorLeft.setTargetPosition(collectionHeight);
        pivotMotorRight.setTargetPosition(collectionHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotNormal() {
        pivotMotorLeft.setTargetPosition(normalHeight);
        pivotMotorRight.setTargetPosition(normalHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotReset() {
        pivotMotorLeft.setTargetPosition(resetHeight);
        pivotMotorRight.setTargetPosition(resetHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotSpecDone() {
        pivotMotorLeft.setTargetPosition(-2001);
        pivotMotorRight.setTargetPosition(-2001);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }
    public void pivotLongNormal() {
        pivotMotorLeft.setTargetPosition(longNormalHeight);
        pivotMotorRight.setTargetPosition(longNormalHeight);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public void pivotZero() {
        pivotMotorLeft.setTargetPosition(75);
        pivotMotorRight.setTargetPosition(75);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorRight.setPower(pivotSpeed);
        pivotMotorLeft.setPower(pivotSpeed);
    }

    public int getAveragePosition() {
        return (pivotMotorLeft.getCurrentPosition() + pivotMotorRight.getCurrentPosition()) / 2;
    }

    public void goUp(){
            pivotMotorLeft.setTargetPosition(pivotMotorLeft.getCurrentPosition()-50);
            pivotMotorRight.setTargetPosition(pivotMotorRight.getCurrentPosition()-50);
            pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            pivotMotorRight.setPower(pivotSpeed);
            pivotMotorLeft.setPower(pivotSpeed);

    }
    public void resetEncoders(){
        pivotMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setPower(double val) {
        pivotMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        pivotMotorLeft.setPower(val);
        pivotMotorRight.setPower(val);
    }

    @Override
    public void periodic() {
        autoUpdatePivot();
    }

}