package frc.robot;

import java.util.Map;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.networktables.NetworkTableEntry;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class DashboardControlSystem {
  private static ShuffleboardTab teleopTab = Shuffleboard.getTab("Teleop");
  private static ShuffleboardTab endgameTab;
  private static NetworkTableEntry intakeLifterSpeed =
  teleopTab.add("Intake Lifter Volt", 0).withPosition(5, 0)
        .getEntry();
  private static NetworkTableEntry intakeLifterPos =
  teleopTab.add("Intake Lifter Position", 0).withPosition(0, 0)
        .getEntry();
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());

  private static NetworkTableEntry teleTime;

  public static void initialize(MecanumDriveSubsystem drive, GyroSubsystem gyro, BallSubsystem balls, ClimbingSubsystem climb, ArmLifterSubsystem lifter) {
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
      new RaiseBallsPos(lifter, intakeLifterPos, 1))
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
     new RaiseBallsPos(lifter, -Constants.intakeLifterDownPosition + 0.1, 0.7), new InstantCommand(() -> lifter.resetPos()));
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


    endgameTab = Shuffleboard.getTab("Endgame");
    // NetworkTableEntry endgameMoveSpeed =
    //   endgameTab.add("DriveTrainSpeed", 0.2)
    //     .getEntry();
    // NetworkTableEntry massMoverSpeed =
    //   endgameTab.add("Mass mover speed", 0.1)
    //     .getEntry();

    // NetworkTableEntry massMoverVolt =
    // endgameTab.add("Mass mover Volt", 0.1)
    //   .getEntry();
    
    NetworkTableEntry HooksVolt = endgameTab.add("Hooks volt", 0.5).getEntry();

    NetworkTableEntry hookPosition = endgameTab.add("Hook position", 1).getEntry();
    // NetworkTableEntry MassMoverPosition = endgameTab.add("Mass Mover position", 1).getEntry();

    endgameTab.add("Raise Hooks", new RaiseHooksPos(climb, hookPosition));
    endgameTab.add("Hooks voltage", new StartEndCommand(() -> climb.runWinch(HooksVolt.getDouble(0.2)), () -> climb.stopWinch(), climb));
    // endgameTab.add("spin mass mover", new MoveMassMoversPos(massMover, MassMoverPosition, massMoverSpeed));
    // endgameTab.add("mass mover voltage", new StartEndCommand(() -> massMover.runMassMover(massMoverVolt.getDouble(0.2)), () -> massMover.stopMassMover(), massMover));

    // ShuffleboardTab climb10ptTab = Shuffleboard.getTab("climb10ptTab");

    // BaseClimbCommand highClimbStep1 = new BaseClimbCommand(climb, balls, lifter, massMover, 20, 5, Constants.intakeLifterEndgame10ptUpPosition, massMoverSpeed);
    // highClimbStep1.setName("10 pt climb, robot in the endzone");
    // BaseClimbCommand highClimbStep2 = new BaseClimbCommand(climb, balls, lifter, massMover, Constants.endgameMassAtWeelie, Constants.hookDeployed10pt, Constants.intakeLifterEndgame10ptUpPosition, massMoverSpeed);
    // highClimbStep2.setName("10 pt climb, robot under the bar at mark");
    // // BaseClimbCommand highClimbStep3 = new BaseClimbCommand(climb, balls, lifter, massMover, Constants.endgameMassAtWeelie, Constants.hookRetracted10pt, Constants.intakeLifterEndgame10ptUpPosition, massMoverSpeed);
    // RaiseHooksPos highClimbStep3 = new RaiseHooksPos(climb, Constants.hookRetracted10pt);
    // highClimbStep3.setName("10 pt climb, hooks on the bar");

    // climb10ptTab.add("10 pt climb step 1", highClimbStep1);
    // climb10ptTab.add("10 pt climb step 2", highClimbStep2);
    // climb10ptTab.add("10 pt climb step 3", highClimbStep3);

    ShuffleboardTab climb6ptTab = Shuffleboard.getTab("climb 6pt Tab");

    RaiseBallsPos mediumClimbStep1_1 = new RaiseBallsPos(lifter, Constants.intakeLifterVertical, 1);
    RaiseHooksPos mediumClimbStep1_2 = new RaiseHooksPos(climb, 4);
    ParallelCommandGroup mediumClimbStep1 = new ParallelCommandGroup(mediumClimbStep1_1, mediumClimbStep1_2);
    mediumClimbStep1.setName("6pt raise arm and deploy hooks");
    
    RaiseHooksPos mediumClimbStep2_1 = new RaiseHooksPos(climb, 0);
    RaiseBallsPos mediumClimbStep2_2 = new RaiseBallsPos(lifter, Constants.intakeLifter6ptDone, 1);
    SequentialCommandGroup mediumClimbStep2 = new SequentialCommandGroup(mediumClimbStep2_1, mediumClimbStep2_2);
    mediumClimbStep2.setName("6pt retract");
    climb6ptTab.add("Climb step 1", mediumClimbStep1);
    climb6ptTab.add("Climb step 2", mediumClimbStep2);



    // climb6ptTab.add("");



    teleTime = teleopTab.add("Time Left", 135).withWidget(BuiltInWidgets.kDial)
    .withProperties(Map.of("min", 0, "max", 135)).withPosition(0, 3).withSize(2, 1).getEntry();
  }

  public static void setTeleTime(double time){
    teleTime.setDouble(time);
  }
}




