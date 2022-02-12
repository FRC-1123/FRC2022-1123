package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.drive.AdvancedMecanumDrive;

import java.util.logging.Logger;

import com.ctre.phoenix.motorcontrol.can.*;
import frc.robot.Constants;

public class MecanumDriveSubsystem extends SubsystemBase {
  private final Logger logger = Logger.getLogger(this.getClass().getName());
  private AdvancedMecanumDrive m_robotDrive;

  /**
   * Creates a new MecanumDriveSubsystem.
   */
  public MecanumDriveSubsystem() {
    WPI_TalonFX frontLeft = new WPI_TalonFX(Constants.kFrontLeftChannel);
    WPI_TalonFX rearLeft = new WPI_TalonFX(Constants.kRearLeftChannel);
    WPI_TalonFX frontRight = new WPI_TalonFX(Constants.kFrontRightChannel);
    WPI_TalonFX rearRight = new WPI_TalonFX(Constants.kRearRightChannel);

    m_robotDrive = new AdvancedMecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
    logger.info("The mecanum drive subsystem is initialized.");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // TODO: Implement periodic functionality such as updating dashboards/logs
  }

  public void driveCartesian(double yval, double xval, double zval, double throttle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.driveCartesian(-yval, -xval, zval, throttle);
  }

  public void FieldCartesian(double yval, double xval, double zval, double throttle, double angle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.driveCartesian(-yval, -xval, zval, throttle, angle);
  }

  public void pivotCartesian(double yval, double xval, double zval, double throttle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.pivotCartesian(-yval, -xval, zval, throttle);
  }

  public void drivePolar(double magnitude, double angle, double zRotation){
    m_robotDrive.drivePolar(magnitude, angle, zRotation);
  }
}