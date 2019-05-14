import RPi.GPIO as gpio

import time

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)

# init sonic sencor

sonic = {'a_trig': 3, 'a_echo': 2, 'b_trig': 14, 'b_echo': 15}

gpio.setup(sonic['a_trig'], gpio.OUT)
gpio.setup(sonic['a_echo'], gpio.IN)
gpio.setup(sonic['b_trig'], gpio.OUT)
gpio.setup(sonic['b_echo'], gpio.IN)

# init motor


motor_pin = 18
gpio.setup(moter_pin, gpio.OUT)
pwm_motor = gpio.PWM(moter_pin, 50)
pwm_motor.start(0)

# init led


led = {'red': 21, 'green': 20, 'blue': 16}

gpio.setup(led['red'], gpio.OUT)
gpio.setup(led['green'], gpio.OUT)
gpio.setup(led['blue'], gpio.OUT)

pwm_red = gpio.PWM(led['red'], 1000)
pwm_green = gpio.PWM(led['green'], 1000)
pwm_blue = gpio.PWM(led['blue'], 1000)

pwm_red.start(100)
pwm_green.start(0)
pwm_blue.start(0)


def updateled(color, duty):
    led_pwm[color].ChangeDutyCycle(float(duty))


def open():
    p.ChangeDutyCycle(6)
    time.sleep(0.8)
    p.ChangeDutyCycle(0)


def close():
    time.sleep(0.8)
    p.ChangeDutyCycle(2)
    time.sleep(0.8)
    p.ChangeDutyCycle(0)


def distance(trig, echo):
    start = 0
    end = 0
    gpio.output(trig, False)
    time.sleep(0.2)
    gpio.output(trig, True)
    time.sleep(0.00001)

    gpio.output(trig, False)

    while gpio.input(echo) == 0:
        start = time.time()

    while gpio.input(echo) == 1:
        end = time.time()

    pulse_duration = end - start
    d = pulse_duration / 0.000058

    if trig == 3:
        print("first_distance:", d, "cm")
    else:
        print("second_distance:", d, "cm")

    return d


def second_ultrasonic(s):

    elem = {'start_time': s, 'end_time': s, 'init_distance': 0, 'flag': 0}

    while elem['end_time'] - elem['start_time'] <= 10:

        d = distance(sonic['b_trig'], sonic['b_echo'])

        if elem['flag'] == 0:
            elem['init_distance'] = d
            elem['flag'] = 1

        elif elem['flag'] == 1:
            if abs(elem['init_distance'] - d) > 10:
                elem['flag'] = 2

        elif elem['flag'] == 2:
            # close door
            if abs(elem['init_distance'] - d) < 5:
                close()
                updateled('red', 100)
                updateled('blue', 0)

                print("out")
                return True

        elem['end_time'] = time.time()

    close()
    updateled('red', 100)
    updateled('blue', 0)

    return False


def first_ultrasonic(s):
    start_time = s
    end_time = s

    updateled('red', 0)
    updateled('blue', 100)

    while end_time - start_time <= 10:

        d = distance(sonic['a_trig'], sonic['a_echo'])

        if d < 20:
            print("in")
            open()
            return second_ultrasonic(time.time())

        end_time = time.time()

    return False

# test
# first_ultrasonic(time.time())
