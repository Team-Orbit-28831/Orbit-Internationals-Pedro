package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LConstants {
    static {
//        TwoWheelConstants.forwardTicksToInches = .0019;
//        TwoWheelConstants.strafeTicksToInches = .002;
//        TwoWheelConstants.forwardY = -3.2;
//        TwoWheelConstants.strafeX = -3.5;
//        TwoWheelConstants.forwardEncoder_HardwareMapName = "front_right_drive";
//        TwoWheelConstants.strafeEncoder_HardwareMapName = "front_left_drive";
//        TwoWheelConstants.forwardEncoderDirection = Encoder.FORWARD;
//        TwoWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
//        TwoWheelConstants.IMU_HardwareMapName = "imu";
//        TwoWheelConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP);
        PinpointConstants.forwardY = -3.2;
        PinpointConstants.strafeX = -3.5;
        PinpointConstants.distanceUnit = DistanceUnit.INCH;
        PinpointConstants.hardwareMapName = "pinpoint";
        PinpointConstants.useYawScalar = false;
        PinpointConstants.yawScalar = 1.0;
        PinpointConstants.useCustomEncoderResolution = false;
        PinpointConstants.encoderResolution = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        PinpointConstants.customEncoderResolution = 13.26291192;
        PinpointConstants.forwardEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        PinpointConstants.strafeEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
    }
}

