package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
// import java.util.logging.Logger;
import frc.robot.subsystems.MassMoverSubsystem;


/**
 * An example command that uses an example subsystem.
 */
public class MoveMassMoversPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  MassMoverSubsystem mover;
  double position;
  double speed;
  NetworkTableEntry positionEntry;
  NetworkTableEntry speedEntry;
  public MoveMassMoversPos(MassMoverSubsystem mover, NetworkTableEntry position, NetworkTableEntry speed) {
      this.mover = mover;
      this.position = position.getDouble(0);
      this.positionEntry = position;
      this.speed = speed.getDouble(0);
      this.speedEntry = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(mover);
    }

    public MoveMassMoversPos(MassMoverSubsystem mover, double position, NetworkTableEntry speed) {
      this.mover = mover;
      this.position = position;
      this.speed = speed.getDouble(0);
      this.speedEntry = speed;
      addRequirements(mover);
    }

    public MoveMassMoversPos(MassMoverSubsystem mover, double position, double speed) {
      this.mover = mover;
      this.position = position;
      this.speed = speed;
      addRequirements(mover);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(speedEntry != null){
      speed = speedEntry.getDouble(0);
    }
    if(positionEntry != null){
      position = positionEntry.getDouble(0);
    }
    position = position * 56.8; //change this assumes a 10 to 1 this is in degrees 2048 * 10 / 360
    move();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    move();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    mover.stopMassMover();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double massMoverDelta = mover.getMassMoverPosition()-mover.getMassMoverStartPosition() - position;
    if(Math.abs(massMoverDelta) < 4000){
      return true;
    }
    return false;
  }

  private void move(){
    double massMoverDelta = mover.getMassMoverPosition()-mover.getMassMoverStartPosition() - position;
    if(massMoverDelta > 0){
      if(Math.abs(massMoverDelta) > 30000)
        mover.runMassMover(speed);
      else if(Math.abs(massMoverDelta) < 7000)
        mover.runMassMover(speed/3);
      else 
        mover.runMassMover(speed/2);
    }
    else {
      if(Math.abs(massMoverDelta) > 30000)
        mover.runMassMover(-speed);
      else if(Math.abs(massMoverDelta) < 7000)
        mover.runMassMover(-speed/3);
      else
        mover.runMassMover(-speed/2);
    }
  }
}