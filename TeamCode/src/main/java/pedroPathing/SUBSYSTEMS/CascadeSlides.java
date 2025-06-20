package pedroPathing.SUBSYSTEMS;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class CascadeSlides implements Subsystem {

    // Slide positions
    private static final double SLIDES_LOW_CHAMBER = 0;
    private static final double SLIDES_HIGH_CHAMBER = 250;
    private static final double SLIDES_LOW_BASKET = 215;
    private static final double SLIDES_HIGH_BASKET = 700;
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
        slideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        slideMotor.setPower(0);
        this.telemetry = telemetry;
        timer.reset();
    }

    public void setSlideTarget(double target) {
        CascadeSlides.target = target;
        pidf.setSetPoint(target);
        autoUpdateSlides();
    }

    public void slideLowBasket() {
        setSlideTarget(SLIDES_LOW_BASKET);
    }

    public void slideHighBasket() {
        setSlideTarget(SLIDES_HIGH_BASKET);
    }

    public void slideHighChamber() {
        setSlideTarget(SLIDES_HIGH_CHAMBER);
    }

    public void slideLowChamber() {
        setSlideTarget(SLIDES_LOW_CHAMBER);
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