package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
        (name="Genius Olympiad Robot win №1 100% Goyda")
public class TeleOP extends LinearOpMode {
    private boolean isHighSpeed = true;
    ColorSensor colorSensor;
    DcMotor motor4;
    DcMotor motor3;
    DcMotor motor2;
    DcMotor motor1;
    DcMotor extendTelescopiaMotorLeft;
    DcMotor extendTelescopiaMotorRight;
    DcMotor liftingMotor;
    DcMotor clawExtendingMotor;
    Servo clawgrip;
    Servo clawlift;
    Servo clawrotate;
    Servo boxopen;
    float servo1StartPos = 1;
    float servo2StartPos = 0.08f;
    float servo3StartPos = 0.13f;
    float servo4StartPos = 0;
    boolean greenDetected = false;
    int m=0;
    int n = 0;
    private float driveSpeed = 1.0f;
    float powerFL;
    float powerFR;
    float powerBL;
    float powerBR;
    boolean Detect = true;
    float changeSpeedGreen =1f;

    double endGameStart;

    boolean isEndGame;

    @Override
public void runOpMode() {
    motor1 = hardwareMap.get(DcMotor.class, "motor1");
    motor2 = hardwareMap.get(DcMotor.class, "motor2");
    motor3 = hardwareMap.get(DcMotor.class, "motor3");
    motor4 = hardwareMap.get(DcMotor.class, "motor4");
    extendTelescopiaMotorLeft = hardwareMap.get(DcMotor.class, "motor5");
    extendTelescopiaMotorRight = hardwareMap.get(DcMotor.class, "motor6");
    liftingMotor = hardwareMap.get(DcMotor.class, "motor8");
    clawExtendingMotor = hardwareMap.get(DcMotor.class, "motor7");

    clawgrip = hardwareMap.get(Servo.class,"servo1");
    clawlift = hardwareMap.get(Servo.class,"servo2");
    clawrotate = hardwareMap.get(Servo.class,"servo3");
    boxopen = hardwareMap.get(Servo.class,"servo4");

    colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");


    motor1.setDirection(DcMotorSimple.Direction.REVERSE);
    motor2.setDirection(DcMotorSimple.Direction.FORWARD);
    motor3.setDirection(DcMotorSimple.Direction.REVERSE);
    motor4.setDirection(DcMotorSimple.Direction.FORWARD);

    extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    extendTelescopiaMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    extendTelescopiaMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    liftingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    liftingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    clawgrip.setPosition(servo1StartPos);
    clawlift.setPosition(servo2StartPos);
    clawrotate.setPosition(servo3StartPos);
    boxopen.setPosition(servo4StartPos);

    waitForStart();
    while (opModeIsActive()) {
        endGameStart = getRuntime()  + 90;
        if (endGameStart >= getRuntime() && !isEndGame) {
            gamepad1.rumbleBlips(3);
            isEndGame = true;
        }
        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();

        if ((green > 120 || red >120 || blue>120)) {
            greenDetected = true;
        }
        else {
            greenDetected = false;
        }

        if (greenDetected) {

        }

        if (gamepad1.touchpad) {
            toggleDriveSpeed();
        }

        Movement();
        Manipulator();
        Claw();
        // OPEN & CLOSE THE BOX
        if (gamepad2.triangle) {
            boxopen.setPosition(0.1);
        }
        if (gamepad2.square) {
            boxopen.setPosition(0);
        }

        if (clawlift.getPosition()==1 && liftingMotor.getCurrentPosition()<-790){
            sleep(300);
            clawgrip.setPosition(servo1StartPos);
        }

        telemetry.addData("GreenDetect",greenDetected);
        telemetry.addData("detect",Detect);
        telemetry.addData("Red", red);
        telemetry.addData("Green", green);
        telemetry.addData("Blue", blue);
        telemetry.addData("posleft",extendTelescopiaMotorLeft.getCurrentPosition());
        telemetry.addData("posright",extendTelescopiaMotorRight.getCurrentPosition());
        telemetry.addData("liftpos",liftingMotor.getCurrentPosition());
        telemetry.addData("servo1",clawgrip.getPosition());
        telemetry.addData("servo2",clawlift.getPosition());
        telemetry.addData("servo3",clawrotate.getPosition());

        telemetry.update();
    }
}
    void Movement() {
        double drive = -gamepad1.left_stick_x; // вперед/назад
        double strafe = gamepad1.left_stick_y; // влево/вправо
        double rotate = gamepad1.right_stick_x; // вращение

        // Расчёт мощности для каждого мотора
        double power1 = drive + strafe + rotate;  // переднее левое
        double power2 = drive - strafe - rotate;  // переднее правое
        double power3 = drive - strafe + rotate;  // заднее левое
        double power4 = drive + strafe - rotate;  // заднее правое

        // Ограничение мощности в диапазоне [-1, 1]
        power1 = Math.max(-1, Math.min(power1, 1));
        power2 = Math.max(-1, Math.min(power2, 1));
        power3 = Math.max(-1, Math.min(power3, 1));
        power4 = Math.max(-1, Math.min(power4, 1));

        // Установка мощности моторов
        motor1.setPower(power1);
        motor2.setPower(power2);
        motor3.setPower(power3);
        motor4.setPower(power4);
    }


    void Manipulator(){
    if (gamepad2.right_bumper){
        extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendTelescopiaMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendTelescopiaMotorLeft.setTargetPosition(2100);
        extendTelescopiaMotorRight.setTargetPosition(2100);
        extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extendTelescopiaMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extendTelescopiaMotorLeft.setPower(1);
        extendTelescopiaMotorRight.setPower(1);
    }
    if (gamepad2.left_bumper){
        extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendTelescopiaMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendTelescopiaMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extendTelescopiaMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extendTelescopiaMotorLeft.setTargetPosition(0);
        extendTelescopiaMotorRight.setTargetPosition(0);
        extendTelescopiaMotorLeft.setPower(-1);
        extendTelescopiaMotorRight.setPower(-1);
    }
}
void Claw(){
    float extend = gamepad2.right_trigger;
    float shift = gamepad2.left_trigger;

    // EXTENDING CLAW //

    // EXTEND //
    if (extend>0){
        clawExtendingMotor.setPower(extend);
    }

    // SHIFT //
    if (shift>0){
        clawExtendingMotor.setPower(-shift);
    }

    // NOTHING //
    if (shift ==0 && extend == 0){
        clawExtendingMotor.setPower(0);
    }

    // LIFTING CLAW //
    if (gamepad2.dpad_up && n==0){  // gp2
        n=1;
        clawlift.setPosition(1);
        liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftingMotor.setTargetPosition(-780);
        liftingMotor.setPower(-1);


    }
    if (gamepad2.dpad_down && n==1){  // gp2
        n=0;
        clawlift.setPosition(0);
        liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftingMotor.setTargetPosition(-5);
        liftingMotor.setPower(0.4);
        m=0;


    }
    if (gamepad1.triangle){
        clawrotate.setPosition((servo3StartPos));
    }
    if (gamepad1.square){
        clawrotate.setPosition((servo3StartPos+0.50));
    }
    if (gamepad1.a){
        clawgrip.setPosition(0.5);
    }
    if (gamepad1.b){
        clawgrip.setPosition(servo1StartPos);
    }
    if (gamepad2.dpad_right){ // gp 2
        clawlift.setPosition(0.4);
    }
    if (gamepad2.dpad_left){ // gp2
        clawlift.setPosition(servo2StartPos);
    }

    if (gamepad2.a){
        clawrotate.setPosition(servo3StartPos + 0.25);
    }
    if (gamepad2.b){
        clawrotate.setPosition(servo3StartPos + 0.75);
    }

    // CLOSE & OPEN CLAW




}

private void toggleDriveSpeed() {
    sleep(100); // задержка, чтобы не дергалось многократно
    isHighSpeed = !isHighSpeed;
    driveSpeed = isHighSpeed ? 1.0f : 0.4f;
}

}



/*

 */
