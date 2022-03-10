package frc.robot.commands;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallSubsystem;
// import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class ShootBalls extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  BallSubsystem balls;
  int endTime;
/**
 * @param time time in seconds for ball to shoot
 */
  public ShootBalls(BallSubsystem balls, double endtime) {
      this.balls = balls;
      this.endTime = endTime * 10000;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(balls);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    balls.spinIntake(-1);
    time = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    balls.spinIntake(-1);
    // logger.info("in shoot balls " + time);
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    balls.spinIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(time > 200){
      return true;
    }
    return false;
  }

}