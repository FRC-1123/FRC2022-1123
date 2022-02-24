package frc.robot.SendableWrapperClasses;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.function.BooleanConsumer;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SendableSparkMax extends CANSparkMax implements Sendable{
  RelativeEncoder encoder;
  public SendableSparkMax(int iD, MotorType motorType){
    super(iD, motorType);
    encoder = super.getEncoder();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    // TODO Auto-generated method stub
    builder.setSmartDashboardType("Motor Controller");
    builder.setActuator(true);
    builder.setSafeState(() -> super.stopMotor());
    builder.addDoubleProperty("Distance", () -> encoder.getPosition()/encoder.getCountsPerRevolution(), null);
    builder.addDoubleProperty("Speed", () -> encoder.getVelocity()/encoder.getCountsPerRevolution(), null);
    builder.addDoubleProperty("Set Point", () -> super.getAppliedOutput(),
      (double input)-> super.set(input));
    // builder.addDoubleProperty("Motor Current", () -> super.getOutputCurrent(), null);
    // builder.addDoubleProperty("Motor Temperature", () -> super.getMotorTemperature(), null);
    // builder.addDoubleProperty("Open Loop ramp rate", () -> super.getOpenLoopRampRate(),
    //   (double rate) -> super.setOpenLoopRampRate(rate));
    // builder.addDoubleProperty("Closed Loop ramp rate", () -> super.getClosedLoopRampRate(),
    //   (double rate) -> super.setClosedLoopRampRate(rate));
    builder.update();
  }
}
