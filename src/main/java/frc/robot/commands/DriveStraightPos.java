package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveSubsystem;

import java.util.logging.Logger;

/**
 * An example command that uses an example subsystem.
 */
public class DriveStraightPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  MecanumDriveSubsystem drive;
  double position;
  NetworkTableEntry positionEntry;
  double driveSpeed;
  NetworkTableEntry driveSpeedEntry;
  double startPosition;
  public DriveStraightPos(MecanumDriveSubsystem drive, NetworkTableEntry position, NetworkTableEntry driveSpeed) {
    this.drive = drive;
    this.position = inchToClicks(position.getDouble(0));
    positionEntry = position;
    this.driveSpeed = driveSpeed.getDouble(0.1);
    this.driveSpeedEntry = driveSpeed;
    addRequirements(drive);
  }

  public DriveStraightPos(MecanumDriveSubsystem drive, double position, NetworkTableEntry driveSpeed) {
    this.drive = drive;
    this.position = inchToClicks(position);
    this.driveSpeed = driveSpeed.getDouble(0.1);
    this.driveSpeedEntry = driveSpeed;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  public DriveStraightPos(MecanumDriveSubsystem drive, double position, double driveSpeed) {
    this.drive = drive;
    this.position = inchToClicks(position);
    this.driveSpeed = driveSpeed;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Drive Forward activate");
    startPosition = drive.getFrontLeftPosition();
    if(positionEntry != null){
      position = inchToClicks(positionEntry.getDouble(0));
    }
    if(driveSpeedEntry != null){
      driveSpeed = driveSpeedEntry.getDouble(0.1);
    }
    drive();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.driveCartesian(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs((drive.getFrontLeftPosition()-startPosition)*.98-position) < 7000){

    }
    return false;
  }

  private double inchToClicks(double inches){
    return inches * 385843;
  }

  private void drive(){
    if((drive.getFrontLeftPosition()-startPosition)*.98-position > 0){
      if(Math.abs((drive.getFrontLeftPosition()-startPosition)*.98-position)> 50000){
        drive.driveCartesian(0, 1, 0, driveSpeed);
      }
      else if(Math.abs((drive.getFrontLeftPosition()-startPosition)*.98-position) < 10000){
        drive.driveCartesian(0, 1, 0, driveSpeed/2);
      }
      else {
        drive.driveCartesian(0, 1, 0, driveSpeed/1.5);
      }
    }
    else {
      if(Math.abs((drive.getFrontLeftPosition()-startPosition)*.98-position)> 50000){
        drive.driveCartesian(0, 1, 0, driveSpeed);
      }
      else if(Math.abs((drive.getFrontLeftPosition()-startPosition)*.98-position) < 10000){
        drive.driveCartesian(0, 1, 0, driveSpeed/2);
      }
      else {
        drive.driveCartesian(0, 1, 0, driveSpeed/1.5);
      }
    }
  }
}