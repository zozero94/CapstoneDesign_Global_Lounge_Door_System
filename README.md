# RaspberryPi

#### 1. 개요

##### 세종대학교 글로벌 라운지 출입 시스템의 출입문 프로토타입 모형을 개발

- 출입문 상태 : RGB_LED 
- 출입문 동작 : 서보모터
- 출입 인증 : 카메라 모듈로 QR 코드 인식 (파이카메라)
- 출입 개폐 : 초음파 센서로 사람과의 거리인식  —> 변경 : 적외선 센서로 거리감지 
- (추가) QR코드 인식 알림 : 엑티브 부저

---

#### 2. 개발환경

- 라즈베리파이3 B+
- 라즈비안 (운영체제)
- Python2 ( simpleCV(openCV)가 python2까지 지원 

---

#### 3. 내용

##### 1. QR code 인식 및 decoding

: ___simpleCV___(open source framework)와 ___zbar___(library)사용 

~~~python
from SimpleCV import Color,Camera,Display
import zbar
cam = Camera()  
display = Display() 

while(display.isNotDone()):
 
 img = cam.getImage() 

 barcode = img.findBarcode() 
 if(barcode is not None): 
   barcode = barcode[0] 
   result = str(barcode.data)
   print result 
   barcode = [] 

 img.save(display) 
~~~

##### 2. 서보모터 제어하기

: __gpio__(library)사용

* 라이브러리설치

  ~~~
  # sudo apt-get update
  # sudo apt-get install python-rpi.gpio
  ~~~

* 코드

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

##### 3. 초음파센서 제어하기

:  __gpio__(library)사용

![image](https://user-images.githubusercontent.com/48287388/55797015-5d043900-5b06-11e9-8ab0-67a93332cfe1.png)

초음파 센서는 20kHz이상의 주파수를 가지는 음파를 발생시키고, 그 음파가 물체에 반사되어오는 시간을 통해 거리를 측정하는 센서

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

##### 3 -2 적외선 센서 제어하기

:  초음파 센서로 거리를 감지하는 부분에서 __input__ 이 들어오지 않으면 __무한루프__ 를 도는 현상이 발생하여 적외선 센서를 이용해 일정거리 내의 물건을 감지하도록 변경함.

~~~python
import RPi.GPIO as gpio
import time

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)

# init sonic sencor
a_out = 23

gpio.setup(a_out,gpio.IN)


def infrared():
    try:
      input_state = gpio.input(a_out)
      if input_state == False:
        print("일정거리 내의 물건을 감지하였습니다.")
    finally:
      gpio.cleanup()


~~~



##### 4. 동기화 작업

:   __서버와 통신__ 을 하는 스레드와 __QRcode__ 를 인식하는 스레드간의 동기화 작업

 (__threading.Lock__ 과 __threading.Event__ 사용)

~~~python
import threading, time
import data
import movement as mv

lock = threading.Lock()
eve = threading.Event()

class Move(threading.Thread):

    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket
        
    def run(self):

        while True:

            try:
                res = self.client_socket.recv(1024)
                res_dic = data.toDict(res)

                if res_dic['seqType'] == 301:
                    lock.acquire()
                    if mv.a_distance(time.time()):
                        self.client_socket.send(data.toString(400))
                        self.client_socket.send(b'\n')
                    else:
                        self.client_socket.send(data.toString(401))
                        self.client_socket.send(b'\n')  
                    lock.release()
                    eve.set()
                elif res_dic['seqType'] == 302:
                    lock.acquire()               
                    time.sleep(1)
                    lock.release()
                    eve.set()
                    continue
                elif res_dic['seqType'] == 700:
                    lock.acquire()
                    mv.a_distance(time.time())
                    self.client_socket.send(data.toString(402))
                    self.client_socket.send(b'\n')
                    lock.release()
                    eve.set()

            except Exception as e:
                print (e)

 

 

class Qr(threading.Thread):


    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket

    def run(self):
        eve.set()
        while True:
            try:
                code = data.toStringQr()
                self.client_socket.send(code.encode())
                self.client_socket.send(b'\n')
                eve.clear()
                eve.wait()
            except Exception as e:
                print (e)

~~~



##### 5. (추가) 엑티브 부저

: 엑티브 부저로 QR코드 인식시 '삐' 소리로 QR코드가 인식 되었다는 것을 사용자에게 알림

~~~python
import RPi.GPIO as gpio
import time

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)

buz = 27
gpio.setup(buz, gpio.OUT, initial = gpio.HIGH)

def buzzer():
    gpio.output(buz,gpio.LOW) # 소리 시작
    time.sleep(0.1)
    gpio.output(buz,gpio.HIGH) # 소리 종료
~~~



