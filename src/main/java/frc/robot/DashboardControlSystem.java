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
  // private static ShuffleboardTab endgameTab;
  private static NetworkTableEntry intakeLifterSpeed =
  teleopTab.add("Intake Lifter Volt", 0).withPosition(5, 0)
        .getEntry();
  private static NetworkTableEntry intakeLifterPos =
  teleopTab.add("Intake Lifter Position", 0).withPosition(0, 0)
        .getEntry();
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());

  public static void initialize(MecanumDriveSubsystem drive, GyroSubsystem gyro, BallSubsystem balls, ClimbingSubsystem climb, ArmLifterSubsystem lifter, MassMoverSubsystem massMover) {
    SmartDashboard.putString("Limelight URL", "http://limelight.local:5801/");
    ShuffleboardLayout intakeRoller = teleopTab.getLayout("Intake roller control", BuiltInLayouts.kList)
      .withPosition(6, 0).withSize(2, 2)
      .withProperties(Map.of("Label Position", "HIDDEN"));
    NetworkTableEntry IntakeSpeed =
      intakeRoller.add("Intake Speed", 0.4)
            .getEntry();
    StartEndCommand intakeToggle = new StartEndCommand(() -> balls.spinIntake(),
      () -> balls.stopIntake(), balls);
    intakeToggle.setName("IntakeMotorToggle");
    intakeRoller.add("Intake Motor Toggle", intakeToggle);

    StartEndCommand intakeDashboardSpeedToggle = new StartEndCommand(() -> 
    balls.spinIntake(IntakeSpeed.getDouble(0.3)),() -> balls.stopIntake(), balls);
    intakeDashboardSpeedToggle.setName("Intake Motor toggle at dashboard speed");
    intakeRoller.add("Intake Motor Toggle at Dashboard Speed", intakeDashboardSpeedToggle);


    teleopTab.add("Intake Lifter Go to Position",
      new RaiseBallsPos(lifter, intakeLifterPos, 0.4))
      .withPosition(1, 0).withSize(2,1);
    StartEndCommand intakeLifterPercent = new StartEndCommand(() -> 
    lifter.driveLifter(intakeLifterSpeed.getDouble(0.3)),() -> lifter.stopLifter(), lifter);
    intakeLifterPercent.setName("Intake Lifter set percent run");
    teleopTab.add("Intake Lifter run voltage", intakeLifterPercent)
      .withPosition(3, 0).withSize(2,1);
    InstantCommand IntakeLifterStop = new InstantCommand(() -> lifter.stopLifter(), lifter);
    IntakeLifterStop.setName("Intake Motor Stop");
    teleopTab.add("Intake Lifter Stop", IntakeLifterStop)
      .withPosition(2, 1).withSize(2,1);

    SequentialCommandGroup calibrateIntakeArmCommand= new SequentialCommandGroup(new CalibrateIntakeArm(lifter),
     new RaiseBallsPos(lifter, -Constants.intakeLifterDownPosition + 0.1, 0.3), new InstantCommand(() -> lifter.resetPos()));
    calibrateIntakeArmCommand.setName("Calibrate Intake Arm And Go up");
    teleopTab.add("Calibrate intake arm and Go up", calibrateIntakeArmCommand)
    .withPosition(0, 1).withSize(2,1);


    teleopTab.add("Calibrate intake arm", new CalibrateIntakeArm(lifter)).withPosition(3, 1).withSize(2,1);

    ShuffleboardTab testingTab = Shuffleboard.getTab("Testing Tab");
    NetworkTableEntry gyroTurnPos = testingTab.add("Gyro turn pos", 0).getEntry();
    testingTab.add(new GyroTurn(drive, gyro, gyroTurnPos));
    NetworkTableEntry driveSpeed = testingTab.add("Drive speed", 0.4).getEntry();
    NetworkTableEntry drivePos = testingTab.add("Drive Straight pos", 0).getEntry();
    testingTab.add(new DriveStraightPos(drive, drivePos, driveSpeed));
    NetworkTableEntry hooksPos = testingTab.add("Hooks pos", 0).getEntry();
    testingTab.add(new RaiseHooksPos(climb, hooksPos));
    InstantCommand calibrateGyro = new InstantCommand(() -> gyro.calibrateGyro(), gyro);
    calibrateGyro.setName("calibrate Gyro");
    testingTab.add(calibrateGyro);


    // endgameTab = Shuffleboard.getTab("Endgame");
    // NetworkTableEntry endgameMoveSpeed =
    //   endgameTab.add("DriveTrainSpeed", 0.2)
    //     .getEntry();
    // NetworkTableEntry massMoverSpeed =
    //   endgameTab.add("Mass mover speed", 0.1)
    //     .getEntry();

    // NetworkTableEntry hookPosition = endgameTab.add("Hook position", 1).getEntry();
    // NetworkTableEntry MassMoverPosition = endgameTab.add("Mass Mover position", 1).getEntry();

    // endgameTab.add("Raise Hooks", new RaiseHooksPos(climb, hookPosition));
    // endgameTab.add("spin mass mover", new MoveMassMoversPos(massMover, MassMoverPosition, massMoverSpeed));

    // BaseClimbCommand highClimbStep1 = new BaseClimbCommand(climb, balls, lifter, massMover, 0, 30, Constants.intakeLifterTopPosition, massMoverSpeed);
    // highClimbStep1.setName("10 pt climb, robot in the endzone");
    // BaseClimbCommand highClimbStep2_1 = new BaseClimbCommand(climb, drive, balls, lifter, massMover, 14, 0, 40, Constants.intakeLifterWheeliePosition, endgameMoveSpeed, massMoverSpeed);
    // BaseClimbCommand highClimbStep2_2 = new BaseClimbCommand(climb, drive, balls, lifter, massMover, 40, 0, 50, Constants.intakeLifterWheeliePosition, endgameMoveSpeed, massMoverSpeed);
    // BaseClimbCommand highClimbStep2_3 = new BaseClimbCommand(climb, drive, balls, lifter, massMover, 6, 180, 72, Constants.intakeLifterWheeliePosition, endgameMoveSpeed, massMoverSpeed);
    // SequentialCommandGroup highClimbStep2 = new SequentialCommandGroup(highClimbStep2_1, highClimbStep2_2, highClimbStep2_3);
    // highClimbStep2.setName("10 pt climb, robot on the wall"); //6ft drive distance to bar.
    // BaseClimbCommand highClimbStep3_1 = new BaseClimbCommand(climb, drive, balls, lifter, massMover, 2, 0, 72, Constants.intakeLifterWheeliePosition, endgameMoveSpeed, massMoverSpeed);//72 inches 
    // BaseClimbCommand highClimbStep3_2 = new BaseClimbCommand(climb, drive, balls, lifter, massMover, 2, 0, 50, Constants.intakeLifterEndgamePosition, endgameMoveSpeed, massMoverSpeed);
    // SequentialCommandGroup highClimbStep3 = new SequentialCommandGroup(highClimbStep3_1, highClimbStep3_2);
    // highClimbStep3.setName("10 pt climb, hooks on the bar");
    // endgameTab.add("10 pt climb step 1", highClimbStep1);
    // endgameTab.add("10 pt climb step 2", highClimbStep2);
    // endgameTab.add("10 pt climb step 3", highClimbStep3);
  }
}