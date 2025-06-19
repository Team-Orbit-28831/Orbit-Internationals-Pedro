package pedroPathing.SUBSYSTEMS;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CascadePivot {

    private DcMotorEx pivotMotorLeft;
    private DcMotorEx pivotMotorRight;

    // PIDF coefficients
    public static double Kp = 0.001;
    public static double Ki = 0.00002;
    public static double Kd = 0.002;
    public static double Kg = 0.00115; // static feedforward

    private double integralSum = 0;
    private double lastError = 0;
    private double power = 0.8;

    private ElapsedTime timer = new ElapsedTime();

    public void init(HardwareMap hardwareMap) {
        pivotMotorLeft = hardwareMap.get(DcMotorEx.class, "pivotLeft");
        pivotMotorRight = hardwareMap.get(DcMotorEx.class, "pivotRight");

        // Set directions; adjust if your motors are mounted differently
        pivotMotorLeft.setDirection(DcMotorEx.Direction.FORWARD);
        pivotMotorRight.setDirection(DcMotorEx.Direction.FORWARD);

        pivotMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        pivotMotorLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotorRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        timer.reset();
    }

//    public void updatePIDF(int targetPosition) {
//        // Average position of both motors for feedback
//        int currentPositionLeft = pivotMotorLeft.getCurrentPosition();
//        int currentPositionRight = pivotMotorRight.getCurrentPosition();
//        double currentPosition = (currentPositionLeft + currentPositionRight) / 2.0;
//
//        double error = targetPosition - currentPosition;
//
//        double dt = timer.seconds();
//        if (dt == 0) dt = 0.01; // avoid divide by zero on first run
//        timer.reset();
//
//        integralSum += error * dt;
//        double derivative = (error - lastError) / dt;
//
//        double output = (Kp * error) + (Ki * integralSum) + (Kd * derivative) + Kg;
//
//        // Normalize output by power variable
//        double motorPower = output * power;
//
//        // Set power to both motors
//        pivotMotorLeft.setPower(motorPower);
//        pivotMotorRight.setPower(motorPower);
//
//        lastError = error;
//    }

    public void stop() {
        pivotMotorLeft.setPower(0);
        pivotMotorRight.setPower(0);
    }

    public int getCurrentPosition() {
        // Return average position of both motors
        int posLeft = pivotMotorLeft.getCurrentPosition();
        int posRight = pivotMotorRight.getCurrentPosition();
        return (posLeft + posRight) / 2;
    }
    public void setPower(double val) {
        pivotMotorLeft.setPower(val);
        pivotMotorRight.setPower(val);

    }
    public void setPos(int val){
        pivotMotorLeft.setTargetPosition(val);
        pivotMotorRight.setTargetPosition(val);
        pivotMotorRight.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        pivotMotorLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }
}