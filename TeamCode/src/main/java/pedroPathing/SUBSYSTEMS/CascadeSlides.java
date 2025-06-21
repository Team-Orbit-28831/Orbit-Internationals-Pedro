package pedroPathing.SUBSYSTEMS;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import pedroPathing.commands.SlidesHighBask;


public class CascadeSlides implements Subsystem {

    // Slide positions
    private static final int SLIDES_LOW_CHAMBER = 0;
    private static final int SLIDES_HIGH_CHAMBER = 250;
    private static final int SLIDES_LOW_BASKET = 215;
    private static final int SLIDES_HIGH_BASKET = 730;

    private static final int SLIDES_SUB = 550;

    public static DcMotorEx slideMotor;

    private ElapsedTime timer = new ElapsedTime();

    public static double p = 0.05;
    public static double i = 0;
    public static double d = 0;
    public static double f = 0 ;
    public static PIDFController pidf = new PIDFController(p, i, d, f);
    public static double target;


    public CascadeSlides(HardwareMap hardwareMap, Telemetry telemetry) {
        slideMotor = hardwareMap.get(DcMotorEx.class, "cascade");
        slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        slideMotor.setPower(0);
        this.telemetry = telemetry;
        timer.reset();
    }

    public void setSlideTarget(double target) {
        CascadeSlides.target = target;
        pidf.setSetPoint(target);
        autoUpdateSlides();
    }

    public void slideLowBasket() {
//        setSlideTarget(SLIDES_LOW_BASKET);
        slideMotor.setTargetPosition(SLIDES_LOW_BASKET);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);

    }

    public void slideHighBasket() {
        slideMotor.setTargetPosition(SLIDES_HIGH_BASKET);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    public void slideSubShort() {
        slideMotor.setTargetPosition(SLIDES_SUB);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    public void slideSubLong() {
        slideMotor.setTargetPosition(SLIDES_HIGH_BASKET);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    public void slideHighChamber() {
        slideMotor.setTargetPosition(SLIDES_HIGH_CHAMBER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    public void retract() {
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    public void slideLowChamber() {
        slideMotor.setTargetPosition(SLIDES_LOW_CHAMBER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
    }

    private static Telemetry telemetry;

    public static void autoUpdateSlides() {
        pidf.setPIDF(p, i, d, f);

        double pos = slideMotor.getCurrentPosition();
        double power = (pidf.calculate(pos, target));

        telemetry.addData("Target Position", target);
        telemetry.addData("Current Position Left", pos);

        telemetry.update();

        slideMotor.setPower(power/2);
    }

    public void stop() {
        slideMotor.setPower(0);
    }

    public int getCurrentPosition() {
        return slideMotor.getCurrentPosition();
    }

    public void setPower(double val) {
        slideMotor.setPower(val);
    }

    @Override
    public void periodic() {
        autoUpdateSlides();
    }
}