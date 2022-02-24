package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;

import java.util.logging.Logger;

/**
 * An example command that uses an example subsystem.
 */
public class CalibrateIntakeArm extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  BallSubsystem balls;
  public CalibrateIntakeArm(BallSubsystem balls) {
    this.balls = balls;
  // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(balls);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Calibrate intake arm");
    time = 0;
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    balls.driveLifter(-0.1);
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    balls.stopLifter();
    balls.resetPos();
    balls.setStartPosition(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if(balls.getLifterCurrent() > 0.8){
    //   return true;
    // }
    if(time > 10 && Math.abs(balls.getLiftMotorVelocity()) < 200){
      return true;
    }
    return false;
  }

  // private double inchToClicks(double inches){
  //   return inches * 385843;
  // }
}