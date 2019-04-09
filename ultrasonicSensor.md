# [RaspberryPi3 B] 초음파센서 제어

#### 1. 초음파 센서 핀

![image](https://user-images.githubusercontent.com/48287388/55797001-51187700-5b06-11e9-9558-b19670d425a8.png)

  ###### 각 타입에 맞게 라즈베리파이에 연결하면된다.

###### trig와 echo 부분은 입출력신호를 받는 핀이라 연결한 gpio 번호를 잘 확인해야한다.

##### 각 타입은 <https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/module/ServoMotor.md>에 작성해 두었다.

#### 2. 초음파 센서 원리

![image](https://user-images.githubusercontent.com/48287388/55797015-5d043900-5b06-11e9-8ab0-67a93332cfe1.png)

초음파 센서는 20kHz이상의 주파수를 가지는 음파를 발생시키고, 그 음파가 물체에 반사되어오는 시간을 통해 거리를 측정하는 센서이다.

#### 3. 라이브러리 설치

필요한 라이브러리설치는 <https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/module/ServoMotor.md>에 작성해 두었다.



#### 4. 코드

~~~python
import RPi.GPIO as gpio
import time

gpio.setmode(gpio.BCM)

trig = 3 # gpio3(5번핀)에 연결했다고 가정
echo = 2 # gpio2 (3번핀)에 연결했다고 가정

gpio.setup(trig,gpio.OUT)
gpio.setup(echo,gpio.IN)

while 1 :
    try :
        gpio.output(trig,False)
        time.sleep(0.5)
        
        gpio.output(trig,True)
        time.sleep(0.00001)
        gpio.output(trig,False) 
        
        while gpio.input(echo) == 0 :
            pulse_start = time.time()
            #time.time() => 1970년 1월 1일 기준으로 흐른시간을 float단위로 리턴
         while gpio.input(echo) == 1 :
            pulse_end = time.time()
        
        pulse_duration =  pulse_end - pulse_start
        distance = pulse_duration * 17000 
        # 대기중 음파의 속력이 340m/s이고, 음파를 발생시키고 반사되는 시간이 반환되기 때문에 
        # 1/2를 해주어야한다. => 170m/s
        # 센티미터로 바꾸어주면 => 17000cm/s
        distance  = round(distance,2)
        print("distance: ",distance,"cm")

    except :
        gpio.cleanup()
    

~~~



