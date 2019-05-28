import RPi.GPIO as gpio
import atexit
import time

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)


# init sonic sencor
a_out = 23
b_out = 24

gpio.setup(a_out,gpio.IN)
gpio.setup(b_out,gpio.IN)

# init motor
motor_pin = 18
gpio.setup(motor_pin, gpio.OUT)
pwm_motor = gpio.PWM(motor_pin, 50)
pwm_motor.start(0)

# init led
led = {'red': 21, 'green': 20, 'blue': 16}
gpio.setup(led['red'], gpio.OUT)
gpio.setup(led['green'], gpio.OUT)
gpio.setup(led['blue'], gpio.OUT)

pwm_red = gpio.PWM(led['red'], 1000)
pwm_green = gpio.PWM(led['green'], 1000)
pwm_blue = gpio.PWM(led['blue'], 1000)
led_pwm ={'red': pwm_red, 'green': pwm_green, 'blue': pwm_blue}

pwm_red.start(100)
pwm_green.start(0)
pwm_blue.start(0)

# init buzzer

buz = 27
gpio.setup(buz, gpio.OUT, initial = gpio.HIGH)

def buzzer():
    gpio.output(buz,gpio.LOW)
    time.sleep(0.1)
    gpio.output(buz,gpio.HIGH)

def updateled(color, duty):
    led_pwm[color].ChangeDutyCycle(float(duty))


def open():
    pwm_motor.ChangeDutyCycle(6)
    time.sleep(0.8)
    pwm_motor.ChangeDutyCycle(0)


def close():
    time.sleep(0.8)
    pwm_motor.ChangeDutyCycle(2)
    time.sleep(0.8)
    pwm_motor.ChangeDutyCycle(0)


def first_infrared(s):
    start_time = s
    end_time = s
    
    updateled('red', 0)
    updateled('green', 100)
    
    while end_time - start_time <= 10:
        
        input_state = gpio.input(a_out)
        if input_state == False:
            open()
            return second_infrared(time.time())
        end_time = time.time()
    
    updateled('red', 100)
    updateled('blue', 0)
    return False

def second_infrared(s):
    start_time = s
    end_time = s
    
    while end_time - start_time <= 10:
        
        input_state = gpio.input(b_out)
        
        if input_state == False:
            time.sleep(1)
            close()
            updateled('red', 100)
            updateled('blue', 0)
            return True
        end_time = time.time()
    
    updateled('red', 100)
    updateled('green', 0)
    return False


@atexit.register
def cleanUp():
    gpio.cleanup()

# test
#first_infrared(time.time())
