//package pedroPathing.TeleOP;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//
//import pedroPathing.SUBSYSTEMS.CascadeSlides;
//
//@TeleOp(name = "CascadeSlides_Movement")
//public class CascadeSlides_Movement extends LinearOpMode {
//
//    public CascadeSlides cascadeSlides = new CascadeSlides();
//    private DcMotorEx slideMotor;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        cascadeSlides.init(hardwareMap);
//        waitForStart();
//
//        while (opModeIsActive()) {
//
//            if (gamepad1.a) {
//                cascadeSlides.moveSlidesTo(100);
//            } else if (gamepad1.b) {
//                cascadeSlides.moveSlidesTo(800);
//            } else if (gamepad1.x) {
//                cascadeSlides.moveSlidesTo(300);
//            }
//            else{
//                cascadeSlides.moveSlidesTo(0);
//cascadeSlides.stop();
//            }
//
////            telemetry.addData("Position", slideMotor.getCurrentPosition());
//            telemetry.update();
//        }
//    }
//}