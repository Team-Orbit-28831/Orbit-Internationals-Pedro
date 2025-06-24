package pedroPathing.SUBSYSTEMS;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw implements Subsystem {

    private Servo clawServo;
    private Servo clawRotServo;
    private Servo clawUDServo; //claw servo up down

    private Limelight3A camera;

    private boolean isClawOpen = false;

    private static final double CLAW_OPEN = 0.0;
    private static final double CLAW_CLOSED = 0.7;
    private static final double CLAW_UP = 1.0;
    private static final double CLAW_DOWN = 0.33;
    private static final double CLAW_FLAT = 0.0;
    private static final double CLAW_DIA = 0.75;

    private static final double CLAW_PERP = 0.75;



    private ElapsedTime timer = new ElapsedTime();

    public void init(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawRotServo = hardwareMap.get(Servo.class, "clawRot");
        clawUDServo = hardwareMap.get(Servo.class, "clawUD");
//        clawUDServo.setDirection(Servo.Direction.REVERSE);


    }

    // Control claw open/close based on buttons (example)
    public void controlClaw(boolean openButton, boolean closeButton) {
        if (openButton) {
            clawServo.setPosition(CLAW_OPEN);
            isClawOpen = true;
            timer.reset();
        } else if (closeButton) {
            clawServo.setPosition(CLAW_CLOSED);
            isClawOpen = false;
            timer.reset();
        }
    }

    // Example of manual claw angle turn control (if you want)
    public void turnClaw(double leftTrigger, double rightTrigger) {
        // Increment or decrement servo position slightly based on triggers
        double pos = clawRotServo.getPosition();
        double change = (rightTrigger - leftTrigger)*0.1; // small step per loop
        pos += change;
        pos = Math.min(1.0, Math.max(0.0, pos));
        clawRotServo.setPosition(pos);
    }

    // Example method for setting claw angle explicitly
    public void angleClaw(boolean increase, boolean decrease) {
        double pos = clawServo.getPosition();
        if (increase) pos += 0.01;
        if (decrease) pos -= 0.01;
        pos = Math.min(1.0, Math.max(0.0, pos));
        clawServo.setPosition(pos);
    }

    public void setServoPosOC(double val) {
        clawServo.setPosition(val);
    }

    public void setServoPosUD(double val) {
        clawUDServo.setPosition(val);

    }

    public void openClaw() {
        clawServo.setPosition(CLAW_OPEN);
    }

    public void closeClaw() {
        clawServo.setPosition(CLAW_CLOSED);
    }

    public void midpoint() {
        clawServo.setPosition(0.35);
    }

    public void upClaw() {
        clawUDServo.setPosition(CLAW_UP);
    }

    public void downClaw() {
        clawUDServo.setPosition(CLAW_DOWN);
    }


    public void defaultPos() {
        clawRotServo.setPosition(CLAW_FLAT);
    }

//    public void diagPos() {
//        clawRotServo.setPosition(CLAW_DIA);
//    }

    public void flatPos() {
        clawRotServo.setPosition(CLAW_FLAT);
    }

    public void perpPos() {
        clawRotServo.setPosition(CLAW_PERP);
    }

    public void setServoPosRot(double val) {
        clawRotServo.setPosition(val);
    }

    public double getCurrentUDPos() {
        return clawUDServo.getPosition();
    }

    public double getCurrentOCPos() {return clawRotServo.getPosition();}

    public double getCurrentROTPos() {return clawRotServo.getPosition();}

}