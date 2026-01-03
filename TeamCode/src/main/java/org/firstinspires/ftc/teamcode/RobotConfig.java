package org.firstinspires.ftc.teamcode;

/**
 * Центральная конфигурация робота
 * Содержит все константы, имена устройств и настройки
 */
public class RobotConfig {
    
    // ==================== ИМЕНА УСТРОЙСТВ ====================
    
    // Drive motors
    public static final String FRONT_LEFT_MOTOR = "motor1";
    public static final String FRONT_RIGHT_MOTOR = "motor2";
    public static final String BACK_LEFT_MOTOR = "motor3";
    public static final String BACK_RIGHT_MOTOR = "motor4";
    
    // Manipulator motors
    public static final String TELESCOPIC_LEFT_MOTOR = "motor5";
    public static final String TELESCOPIC_RIGHT_MOTOR = "motor6";
    
    // Claw motors
    public static final String CLAW_EXTENDING_MOTOR = "motor7";
    public static final String LIFTING_MOTOR = "motor8";
    
    // Servos
    public static final String CLAW_GRIP_SERVO = "servo1";
    public static final String CLAW_LIFT_SERVO = "servo2";
    public static final String CLAW_ROTATE_SERVO = "servo3";
    public static final String BOX_SERVO = "servo4";
    
    // Sensors
    public static final String COLOR_SENSOR = "colorSensor";
    
    // ==================== SERVO ПОЗИЦИИ ====================
    
    // Claw grip
    public static final double CLAW_GRIP_OPEN = 1.0;
    public static final double CLAW_GRIP_CLOSED = 0.5;
    
    // Claw lift
    public static final double CLAW_LIFT_DOWN = 0.08;
    public static final double CLAW_LIFT_UP = 1.0;
    public static final double CLAW_LIFT_MIDDLE = 0.4;
    
    // Claw rotate
    public static final double CLAW_ROTATE_DEFAULT = 0.13;
    public static final double CLAW_ROTATE_90 = 0.63;   // +0.50
    public static final double CLAW_ROTATE_45 = 0.38;   // +0.25
    public static final double CLAW_ROTATE_180 = 0.88;  // +0.75
    
    // Box
    public static final double BOX_OPEN = 0.1;
    public static final double BOX_CLOSED = 0.0;
    
    // ==================== ПОЗИЦИИ МОТОРОВ ====================
    
    // Telescopic extension
    public static final int TELESCOPIC_EXTENDED = 2100;
    public static final int TELESCOPIC_RETRACTED = 0;
    
    // Lifting
    public static final int LIFTING_UP_POSITION = -780;
    public static final int LIFTING_DOWN_POSITION = -5;
    
    // ==================== МОЩНОСТЬ МОТОРОВ ====================
    
    public static final double TELESCOPIC_EXTEND_POWER = 1.0;
    public static final double TELESCOPIC_RETRACT_POWER = 1.0;
    public static final double LIFTING_UP_POWER = 1.0;
    public static final double LIFTING_DOWN_POWER = 0.4;
    
    // ==================== НАСТРОЙКИ ДРАЙВА ====================
    
    public static final double HIGH_SPEED = 1.0;
    public static final double LOW_SPEED = 0.4;
    
    // ==================== ИГРОВЫЕ КОНСТАНТЫ ====================
    
    public static final double ENDGAME_WARNING_TIME = 90.0; // секунды
    public static final int COLOR_DETECTION_THRESHOLD = 120;
    
    // ==================== TIMING ====================
    
    public static final int DEBOUNCE_DELAY_MS = 150;
    public static final int AUTO_GRIP_DELAY_MS = 300;
    
    // Предотвращение создания экземпляров
    private RobotConfig() {}
}
