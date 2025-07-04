    package pedroPathing.SUBSYSTEMS;

    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.HardwareMap;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.Telemetry;

    public class Drivetrain {
        private DcMotor frontLeftDrive = null;
        private DcMotor frontRightDrive = null;
        private DcMotor backLeftDrive = null;
        private DcMotor backRightDrive = null;



        // PID variables
        private final double integralSum = 0;
        private final double lastError = 0;
        private final double Kp = 0.1; // Proportional gain
        private final double Ki = 0.01; // Integral gain
        private final double Kd = 0.05; // Derivative gain
        private final ElapsedTime timer = new ElapsedTime();

        public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
            frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");   // CONTROL: port 0
            backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");     // CONTROL: port 1
            backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");       // CONTROL: port 2
            frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");     // CONTROL: port 3

            // Set motor directions (adjust if needed)
            frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
            backRightDrive.setDirection(DcMotor.Direction.REVERSE);


            // Reset timer
            timer.reset();
        }



        public void drive(double leftY, double leftX, double rightX,double drive_sens) {
            // Forward/Backward and Strafing
            double frontLeftPower = leftY + leftX + rightX;
            double frontRightPower = leftY - leftX - rightX;
            double backLeftPower = leftY - leftX + rightX;
            double backRightPower = leftY + leftX - rightX;

            // Apply sensitivity scaling
            frontLeftDrive.setPower(frontLeftPower / drive_sens);
            backLeftDrive.setPower(backLeftPower / drive_sens);
            frontRightDrive.setPower(frontRightPower / drive_sens);
            backRightDrive.setPower(backRightPower / drive_sens);
        }

        public void bDrive(double leftY, double leftX, double rightX,double drive_sens) {
            // Forward/Backward and Strafing
            double frontLeftPower = leftY - leftX - rightX;
            double frontRightPower = leftY + leftX + rightX;
            double backLeftPower = leftY + leftX - rightX;
            double backRightPower = leftY - leftX + rightX;

            // Apply sensitivity scaling
            frontLeftDrive.setPower(frontLeftPower / drive_sens);
            backLeftDrive.setPower(backLeftPower / drive_sens);
            frontRightDrive.setPower(frontRightPower / drive_sens);
            backRightDrive.setPower(backRightPower / drive_sens);
        }

    //    public double PID(double reference, double state) {
    //        double error = reference - state;
    //        double dt = timer.seconds();  // Get elapsed time since last update
    //
    //        integralSum += error * dt;
    //        double derivative = (error - lastError) / dt;
    //        lastError = error;
    //        timer.reset();
    //
    //        return (Kp * error) + (Ki * integralSum) + (Kd * derivative);
    //    }
    }