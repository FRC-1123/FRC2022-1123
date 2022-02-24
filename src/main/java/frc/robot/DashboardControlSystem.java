package frc.robot;

import java.util.Map;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.networktables.NetworkTableEntry;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class DashboardControlSystem {
  private static ShuffleboardTab teleopTab = Shuffleboard.getTab("Teleop");;
  private static ShuffleboardTab endgameTab;
  private static ShuffleboardTab MotorSettings = Shuffleboard.getTab(" Motor Settings");
  private static NetworkTableEntry IntakeSpeed =
  MotorSettings.add("Intake Speed", 0.4)
          .getEntry();
  private static NetworkTableEntry intakeLifterSpeed =
  teleopTab.add("Intake Lifter Volt", 0)
        .getEntry();
  private static NetworkTableEntry intakeLifterPos =
  teleopTab.add("Intake Lifter Position", 0)
        .getEntry();
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());

  public static void initialize(MecanumDriveSubsystem drive, GyroSubsystem gyro, BallSubsystem balls, ClimbingSubsystem climb) {
    SmartDashboard.putData(balls);
    // ShuffleboardLayout motorControl = teleopTab.getLayout("Motor Control", BuiltInLayouts.kList)
    //   .withPosition(0, 0).withSize(2, 2)
    //   .withProperties(Map.of("Label Position", "HIDDEN"));

    StartEndCommand intakeToggle = new StartEndCommand(() -> balls.spinIntake(),
      () -> balls.stopIntake(), balls);
    intakeToggle.setName("IntakeMotorToggle");
    teleopTab.add("Intake Motor Toggle", intakeToggle);

    StartEndCommand intakeDashboardSpeedToggle = new StartEndCommand(() -> 
    balls.spinIntake(IntakeSpeed.getDouble(0.3)),() -> balls.stopIntake(), balls);
    intakeDashboardSpeedToggle.setName("Intake Motor toggle at dashboard speed");
    teleopTab.add("Intake Motor Toggle at Dashboard Speed", intakeDashboardSpeedToggle);

    InstantCommand intakeStop = new InstantCommand(() -> balls.stopIntake(), balls);
    intakeStop.setName("Intake Motor Stop");
    teleopTab.add("Intake Motor Stop", intakeStop);

    teleopTab.add("Intake Lifter Go to Position",
      new RaiseBallsPos(balls, intakeLifterPos));
    StartEndCommand intakeLifterPercent = new StartEndCommand(() -> 
    balls.driveLifter(intakeLifterSpeed.getDouble(0.3)),() -> balls.stopLifter(), balls);
    intakeLifterPercent.setName("Intake Lifter set percent run");
    teleopTab.add("Intake Lifter run voltage", intakeLifterPercent);
    InstantCommand IntakeLifterStop = new InstantCommand(() -> balls.stopLifter(), balls);
    IntakeLifterStop.setName("Intake Motor Stop");
    teleopTab.add("Intake Lifter Stop", IntakeLifterStop);

    SequentialCommandGroup calibrateIntakeArmCommand= new SequentialCommandGroup(new CalibrateIntakeArm(balls), new RaiseBallsPos(balls, Constants.intakeLifterTopPosition));
    calibrateIntakeArmCommand.setName("Calibrate Intake Arm And Go up");
    teleopTab.add("Calibrate intake arm and Go up", calibrateIntakeArmCommand);

    teleopTab.add("Calibrate intake arm", new CalibrateIntakeArm(balls));


    endgameTab = Shuffleboard.getTab("Endgame");
    NetworkTableEntry endgameMoveSpeed =
      endgameTab.add("DriveTrainSpeed", 0.2)
        .getEntry();
    NetworkTableEntry massMoverSpeed =
      endgameTab.add("Mass mover speed", 0.1)
        .getEntry();

    NetworkTableEntry hookPosition = endgameTab.add("Hook position", 1).getEntry();
    NetworkTableEntry MassMoverPosition = endgameTab.add("Mass Mover position", 1).getEntry();

    endgameTab.add("Raise Hooks", new RaiseHooksPos(climb, hookPosition));
    endgameTab.add("spin mass mover", new MoveMassMoversPos(climb, MassMoverPosition, massMoverSpeed));

    BaseClimbCommand highClimbStep1 = new BaseClimbCommand(climb, balls, 0, 30, Constants.intakeLifterTopPosition, massMoverSpeed);
    highClimbStep1.setName("10 pt climb, robot in the endzone");
    BaseClimbCommand highClimbStep2_1 = new BaseClimbCommand(climb, drive, balls, 14, 0, 40, 4, endgameMoveSpeed, massMoverSpeed);
    BaseClimbCommand highClimbStep2_2 = new BaseClimbCommand(climb, drive, balls, 40, 0, 50, 4, endgameMoveSpeed, massMoverSpeed);
    BaseClimbCommand highClimbStep2_3 = new BaseClimbCommand(climb, drive, balls, 6, 180, 72, 4, endgameMoveSpeed, massMoverSpeed);
    SequentialCommandGroup highClimbStep2 = new SequentialCommandGroup(highClimbStep2_1, highClimbStep2_2, highClimbStep2_3);
    highClimbStep2.setName("10 pt climb, robot on the wall"); //6ft drive distance to bar.
    BaseClimbCommand highClimbStep3_1 = new BaseClimbCommand(climb, drive, balls, 2, 0, 72, 4, endgameMoveSpeed, massMoverSpeed);//72 inches 
    BaseClimbCommand highClimbStep3_2 = new BaseClimbCommand(climb, drive, balls, 2, 0, 50, 1, endgameMoveSpeed, massMoverSpeed);
    SequentialCommandGroup highClimbStep3 = new SequentialCommandGroup(highClimbStep3_1, highClimbStep3_2);
    highClimbStep3.setName("10 pt climb, hooks on the bar");
    endgameTab.add("10 pt climb step 1", highClimbStep1);
    endgameTab.add("10 pt climb step 2", highClimbStep2);
    endgameTab.add("10 pt climb step 3", highClimbStep3);

  }

  public static void testModeDisplayInitialize(MecanumDriveSubsystem drive, GyroSubsystem gyro,
    BallSubsystem balls){
  }

  public static void testModeDisplayPeriodic(MecanumDriveSubsystem drive, GyroSubsystem gyro,
    BallSubsystem balls){
      
  }
}