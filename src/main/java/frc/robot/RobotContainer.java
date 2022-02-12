package frc.robot;

import java.util.Map;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class RobotContainer {
  private static NetworkTableEntry teleTime;
  private static NetworkTableEntry endTime;
  private static ShuffleboardTab teleopTab;
  private static ShuffleboardTab endgameTab;
  private static ShuffleboardTab MotorSettings = Shuffleboard.getTab(" Motor Settings");
  private static NetworkTableEntry IntakeSpeed =
  MotorSettings.add("Intake Speed", 0.4)
          .getEntry();
  private static NetworkTableEntry IntakeLifterVolt =
  MotorSettings.add("Intake Lifter Volt", 0)
        .getEntry();
  private static NetworkTableEntry IntakeLifterPos =
  MotorSettings.add("Intake Lifter Position", 0)
        .getEntry();
  private static NetworkTableEntry IntakeLiftkP =
  MotorSettings.add("Intake Lift Motor P", 0.1)
        .getEntry();
    Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());

    private static final MecanumDriveSubsystem Drive = new MecanumDriveSubsystem();
    private static final GyroSubsystem gyro = new GyroSubsystem();
    private static final BallSubsystem ballSubysytem = new BallSubsystem();

  public static void initialize() {
    // MecanumDriveSubsystem Drive = new MecanumDriveSubsystem();
    // GyroSubsystem gyro = new GyroSubsystem();
    // BallSubsystem ballSubysytem = new BallSubsystem();
    JoystickControlSystem.initialize(new Joystick(Constants.kJoystickChannel), Drive, gyro);
    teleopTab = Shuffleboard.getTab("Teleop");
    ShuffleboardLayout motorControl = teleopTab.getLayout("Motor Control", BuiltInLayouts.kList)
      .withPosition(0, 0).withSize(2, 2)
      .withProperties(Map.of("Label Position", "HIDDEN"));

    motorControl.add("Intake Motor Start", new SpinIntake(ballSubysytem));
    motorControl.add("Intake Motor Start at Dashboard speed",
     new SpinIntakeAtSetSpeed(ballSubysytem, IntakeSpeed));
    motorControl.add("Intake Motor Stop", new StopIntake(ballSubysytem));

    motorControl.add("Intake Lifter Go to Position",
      new RaiseBallsPos(ballSubysytem, IntakeLifterPos, IntakeLiftkP));
    motorControl.add("Intake Lifter run voltage",
      new RaiseBallsVolt(ballSubysytem, IntakeLifterVolt));

    // ShuffleboardLayout Misc = teleopTab.getLayout("Misc", BuiltInLayouts.kList)
    //   .withPosition(6, 0).withSize(2, 2)
    //   .withProperties(Map.of("Label Position", "HIDDEN"));
    // Misc.add("Limelight", new LimelightCommand(new LimelightSubsystem()));
    // Misc.add("Calibrate Gyro", new CalibrateGyro());

    teleTime = teleopTab.add("Time Left", 135)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", 0, "max", 135))
      .withPosition(0, 2).withSize(2, 1)
      .getEntry();

    endgameTab = Shuffleboard.getTab("Endgame");

    endTime = endgameTab.add("Time Left", 135)
      .withWidget(BuiltInWidgets.kDial)
      .withProperties(Map.of("min", 0, "max", 135))
      .withPosition(0, 2).withSize(2, 1)
      .getEntry();


    // ShuffleboardTab camTab = Shuffleboard.getTab("piCamera");
    // NetworkTableEntry piTable = camTab.add("Pi Camera", true)
    //   .withWidget(BuiltInWidgets.kCameraStream)
    //   //.withProperties(Map.of("min", 0, "max", 135))
    //   .withPosition(0, 0).withSize(2, 2)
    //   .getEntry();
  }

  // public static void PutIsBox(boolean boo){
  //   SmartDashboard.putBoolean("Limelight Box", boo);
  // }

  public static void putTimeRemaining(double time){
    teleTime.forceSetDouble(time);
    endTime.forceSetDouble(time);
  }
}