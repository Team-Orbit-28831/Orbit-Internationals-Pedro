//package pedroPathing.TeleOP;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import pedroPathing.SUBSYSTEMS.CascadePivot;
//import pedroPathing.SUBSYSTEMS.CascadeSlides;
//import pedroPathing.SUBSYSTEMS.Claw;
//
//@TeleOp(name = "Pivot & Slides Manual PIDF", group = "Test")
//public class pidtestingforstuff extends LinearOpMode {
//
//    private CascadePivot pivot = new CascadePivot();
//    private CascadeSlides slides = new CascadeSlides();
//    private  Claw claw = new Claw();
//
//    private ElapsedTime loopTime = new ElapsedTime();
//
//    // Target position variables
//    private int pivotTarget = 0;
//    private int slidesTarget = 0;
//
//
//    // How fast the target position changes
//    private final int PIVOT_STEP = 10;
//    private final int SLIDES_STEP = 10;
//
//    @Override
//    public void runOpMode() {
//        pivot.init(hardwareMap);
//        slides.init(hardwareMap);
//        claw.init(hardwareMap);
//        loopTime.reset();
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            double pivotInput = -gamepad1.left_stick_y;  // Inverted because up is negative
//            double slidesInput = -gamepad1.right_stick_y;
//
//            // Deadzone filtering
//            if (pivotInput > 0.1) {
//
//                pivot.setPower(0.5);
//            } else if (pivotInput < -0.1) {
//                pivot.setPower(-0.5);
//            } else {
//                pivot.setPower(0);
//            }
//
//            if (slidesInput > 0.1) {
//                slides.setPower(0.5);
//            } else if (slidesInput < -0.1) {
//                slides.setPower(-0.5);
//            } else {
//                slides.setPower(0);
//            }
//            if(gamepad1.right_bumper == true){
//                claw.setServoPosOC(0.5);
//            } else if (gamepad1.left_bumper) {
//                claw.setServoPosOC(0.8);
//            }
//
//            if (gamepad1.a){
//                claw.setServoPosRot(0.5);
//            }
//            else if(gamepad1.b){
//                claw.setServoPosRot(1);
//            }
//            if (gamepad1.y) {
//                claw.setServoPosUD(0);
//            }
//            else if (gamepad1.x){
//                claw.setServoPosUD(1);
//            }
//
//            // Clamp targets to reasonable limits
////            pivotTarget = clamp(pivotTarget, 0, 1500);   // Adjust limits as needed
////            slidesTarget = clamp(slidesTarget, 0, 1500); // Adjust limits as needed
//
//            // Apply PIDF control to reach targets
////            pivot.updatePIDF(pivotTarget);
////            slides.updatePIDF(slidesTarget);
//
//            // Telemetry
//            telemetry.addLine("=== Pivot ===");
//            telemetry.addData("Target", pivotTarget);
//            telemetry.addData("Position", pivot.getCurrentPosition());
//
//            telemetry.addLine("=== Slides ===");
//            telemetry.addData("Target", slidesTarget);
//            telemetry.addData("Position", slides.getCurrentPosition());
//            telemetry.update();
//        }
//
//        pivot.stop();
//        slides.stop();
//    }
//
//    private int clamp(int val, int min, int max) {
//        return Math.max(min, Math.min(max, val));
//    }
//}