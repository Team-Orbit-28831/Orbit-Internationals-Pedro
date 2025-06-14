package pedroPathing.SUBSYSTEMS;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CascadeSlides {

    private DcMotorEx slideMotor;

    // PIDF coefficients
    public static double Kp = 11;
    public static double Ki = 0.00002;
    public static double Kd = 0.002;
    public static double Kg = 0.064;  // static feedforward

    private double integralSum = 0;
    private double lastError = 0;
    private double power = 0.5;

    private ElapsedTime timer = new ElapsedTime();

    public void init(HardwareMap hardwareMap) {
        slideMotor = hardwareMap.get(DcMotorEx.class, "cascade");
        slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        timer.reset();
    }

//    public void updatePIDF(int targetPosition) {
//        double currentPosition = slideMotor.getCurrentPosition();
//        double error = targetPosition - currentPosition;
//
//        double dt = timer.seconds();
//        timer.reset();
//
//        integralSum += error * dt;
//        double derivative = (error - lastError) / dt;
//
//        double output = (Kp * error) + (Ki * integralSum) + (Kd * derivative) + Kg;
//
//        slideMotor.setPower(output/power);
//
//        lastError = error;
//    }

    public void stop() {
        slideMotor.setPower(0);
    }

    public int getCurrentPosition() {
        return slideMotor.getCurrentPosition();
    }
    public void setPower(double val) {
        slideMotor.setPower(val);

    }
}