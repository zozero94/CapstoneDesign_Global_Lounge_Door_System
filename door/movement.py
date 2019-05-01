import RPi.GPIO as gpio
import time

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)

#init sonic sencor
a_trig = 3
a_echo = 2
b_trig = 14
b_echo = 15

gpio.setup(a_trig,gpio.OUT)
gpio.setup(a_echo,gpio.IN)
gpio.setup(b_trig,gpio.OUT)
gpio.setup(b_echo,gpio.IN)

#init motor

pin = 18
gpio.setup(pin,gpio.OUT)
p = gpio.PWM(pin,50)
p.start(0)

def open():

    p.ChangeDutyCycle(6)
    time.sleep(0.8)
    p.ChangeDutyCycle(0)


def close():
    time.sleep(0.8)
    p.ChangeDutyCycle(2)
    time.sleep(0.4)
    p.ChangeDutyCycle(0)

def d(trig , echo):
    gpio.output(trig,False)
    time.sleep(0.2)

    gpio.output(trig,True)
    time.sleep(0.00001)
    gpio.output(trig,False)

    while gpio.input(echo) == 0:
        pulse_start = time.time()
    while gpio.input(echo) == 1:
        pulse_end = time.time()

    pulse_duration = pulse_end - pulse_start
    distance = pulse_duration*17000
    distance = round(distance,2)
    print("distance : ",distance,"cm")
    return distance
    
    
def b_distance(s) :
  
    flag = 0
    initDistance = 0

    start_time = s
    end_time = s
    
    while end_time - start_time <= 10 :
        
        distance = d(b_trig , b_echo)
        
        if flag == 0 :
            initDistance = distance
            flag = 1
        elif flag == 1 :
            if abs(initDistance - distance) > 10 :
                flag = 2
        elif flag == 2:
            if abs(initDistance - distance) < 5 :
                # close the door
                close()
                print("out")
                return True

        
        end_time = time.time()
    
    close()
    return False

  
def a_distance(s) :

    start_time = s
    end_time = s
    
    while end_time - start_time <= 10 :
        
        distance = d(a_trig, a_echo)
       
        if(distance < 20):
            print("in")
            open()
            return b_distance(time.time())
            
        end_time = time.time()
        
    return False

#test
#a_distance(time.time())
 