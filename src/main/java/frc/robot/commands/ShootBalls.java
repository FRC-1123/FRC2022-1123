package frc.robot.commands;

// import java.util.logging.Logger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallSubsystem;


/**
 * An example command that uses an example subsystem.
 */
public class ShootBalls extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  BallSubsystem balls;
  int endTime;
  int time = 0;
/**
 * @param time time in seconds for ball to shoot
 */
  public ShootBalls(BallSubsystem balls, int endTime) {
      this.balls = balls;
      this.endTime = endTime * 50;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(balls);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    balls.spinIntake(-1);
    time++;
    // logger.info("in shoot balls " + time);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    balls.spinIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(time > endTime){
      return true;
    }
    return false;
  }

}