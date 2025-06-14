package pedroPathing.SUBSYSTEMS;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw {

    private Servo clawServo;
    private Servo clawRotServo;
    private Servo clawUDServo; //claw servo up down

    private Limelight3A camera;

    private boolean isClawOpen = false;
    private double clawOpenPosition = 0.8;
    private double clawClosedPosition = 0.2;

    private ElapsedTime timer = new ElapsedTime();

    public void init(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawRotServo = hardwareMap.get(Servo.class,"clawRot");
        clawUDServo = hardwareMap.get(Servo.class,"clawUD");


    }

    // Control claw open/close based on buttons (example)
    public void controlClaw(boolean openButton, boolean closeButton) {
        if (openButton) {
            clawServo.setPosition(clawOpenPosition);
            isClawOpen = true;
            timer.reset();
        } else if (closeButton) {
            clawServo.setPosition(clawClosedPosition);
            isClawOpen = false;
            timer.reset();
        }
    }

    // Example of manual claw angle turn control (if you want)
    public void turnClaw(double leftTrigger, double rightTrigger) {
        // Increment or decrement servo position slightly based on triggers
        double pos = clawServo.getPosition();
        double change = (rightTrigger - leftTrigger) * 0.01; // small step per loop
        pos += change;
        pos = Math.min(1.0, Math.max(0.0, pos));
        clawServo.setPosition(pos);
    }

    // Example method for setting claw angle explicitly
    public void angleClaw(boolean increase, boolean decrease) {
        double pos = clawServo.getPosition();
        if (increase) pos += 0.01;
        if (decrease) pos -= 0.01;
        pos = Math.min(1.0, Math.max(0.0, pos));
        clawServo.setPosition(pos);
    }
    public void setServoPosOC(double val){
        clawServo.setPosition(val);
    }
    public void setServoPosUD(double val){
        clawUDServo.setPosition(val);

    }
    public void setServoPosRot(double val){
        clawRotServo.setPosition(val);
    }
}