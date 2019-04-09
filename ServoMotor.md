# [ RaspberryPi3 B ]서보모터 제어하기



#### 1. 서보모터와 라즈베리파이 배선과 핀

 ![image](https://user-images.githubusercontent.com/48287388/55795815-97200b80-5b03-11e9-8d6a-5b10716d6220.png)



![image](https://user-images.githubusercontent.com/48287388/55796204-583e8580-5b04-11e9-8066-8bfdfb1f6f2c.png)

| servo moter    | RaspberryPi    |
| -------------- | -------------- |
| PWM선 (주황색) | GPIO           |
| 5V (빨간색)    | 2번핀 or 4번핀 |
| GND(갈색)      | GND            |

###### 각 타입에 맞추어 연결해주면 된다. 

###### 단,  gpio부분은 코드상 입출력에 연관이 있으니 몇번핀에 연결했는지 확인 해주어야한다.

#### 2. 라이브러리설치

~~~
sudo apt-get update
~~~

~~~
suod apt-get install python-rpi.gpio
~~~

#### 3. 코드

~~~python
import RPi.GPIO as GPIO
import time
 
pin = 18 # PWM pin num 18
 
GPIO.setmode(GPIO.BCM)
GPIO.setup(pin, GPIO.OUT)
p = GPIO.PWM(pin, 50)
p.start(0)
cnt = 0
try:
    while True:
        p.ChangeDutyCycle(2)
        print "angle : 1"
        time.sleep(1)
        p.ChangeDutyCycle(6)
        print "angle : 5"
        time.sleep(1)
except KeyboardInterrupt:
    p.stop()
GPIO.cleanup()

~~~

