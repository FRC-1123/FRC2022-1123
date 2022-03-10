package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
// import java.util.logging.Logger;
import frc.robot.subsystems.MecanumDriveSubsystem;


/**
 * An example command that uses an example subsystem.
 */
public class DriveReverseTime extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  MecanumDriveSubsystem drive;
  double moveTime;
  double speed;
  /**
   * @param moveTime time to move in seconds
   * @param speed speed to drive at in percent
   */
  public DriveReverseTime(MecanumDriveSubsystem drive, double moveTime, double speed) {
      this.drive = drive;
      this.moveTime = moveTime * 50;// code call per second
      this.speed = speed;
      addRequirements(drive);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive.driveCartesian(0, 1, 0, speed);
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.driveCartesian(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(time > moveTime){
      return true;
    }
    return false;
  }

}