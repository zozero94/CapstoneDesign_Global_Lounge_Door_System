# SimpleCV

------

이미지 처리 라이브러리인 **SimpleCV**에 대해 배운다.

## 설치

1. 다음을 입력해 필요한 패키지들을 설치한다.

   ```bash
   sudo apt-get install ipython python-opencv python-scipy python-numpy python-setuptools python-pip -y
   ```

2. 다음을 입력해 **SimpleCV**를 설치한다.

   ```bash
   mkdir ~/Code
   cd ~/Code
   git clone git://github.com/sightmachine/SimpleCV.git
   cd SimpleCV
   ```

3. **SimpleCV** 폴더에서 의존성 있는 라이브러리 들을 설치한다. 설치시 **PIL** 관련 에러가 발생하는데 무시해도 된다.

   ```bash
   sudo pip install -r requirements.txt
   ```

4. 다음을 입력해 **PIL** 대응 라이브러리인 **Pillow**를 설치한다.

   ```bash
   sudo pip install Pillow
   ```

5. 다음을 입력해 **svgwrite**를 설치한다.

   ```bash
   sudo pip install svgwrite
   ```

6. 기존 **IPython**을 그대로 사용하는 경우 에러가 발생한다. 따라서 다음을 입력해 버젼 4로 바꿔준다.

   ```bash
   pip install 'IPython==4' --force-reinstall
   ```

7. 다음을 입력해 설치를 마무리한다.

   ```bash
   sudo python setup.py develop
   ```

8. 설치가 완료되면 다음을 입력해 **SimpleCV** 콘솔을 실행할 수 있다.

   ```bash
   simplecv
   ```

## GPU 메모리 설정

이미지 처리 성능 향상을 위해 라즈베리 파이의 GPU 메모리를 설정한다.

1. 다음을 입력해 라즈베리 파이 설정을 연다.

   ```bash
   sudo raspi-config
   ```

2. `9 Advanced Options`의 `A3 Memory Split`을 선택한다. 기본 값으로 **128**이 설정되어있다. 이 값을 **256**으로 설정한다.

## 콘솔

1. 다음을 입력해 **SimpleCV** 콘솔을 실행한다.

   ```bash
   simplecv
   ```

2. 콘솔은 **SimpleCV**의 명령어를 하나씩 실행해볼 수 있으며, **SimpleCV**에서 제공하는 다음과 같은 기능을 사용할 수 있다.

   ```
   "exit()" or press "Ctrl+ D" to exit the shell
   "clear" to clear the shell screen
   "tutorial" to begin the SimpleCV interactive tutorial
   "example" gives a list of examples you can run
   "forums" will launch a web browser for the help forums
   "walkthrough" will launch a web browser with a walkthrough
   ```

3. 명령어에 대해 궁금한 것 있다면 `help` 명령어를 사용한다. 다음은 `SimpleCV` 패키지에 대한 설명을 확인하는 것이다.

   ```python
   help(SimpleCV)
   ```

## 이미지

1. **SimpleCV** 콘솔에서 다음을 입력하면 **SimpleCV** 로고를 **logo** 변수에 불러온다.

   ```python
   img = Image("simplecv")
   ```

2. 다음을 입력하면 **SimpleCV** 로고 화면이 나타난다.

   ```python
   img.show()
   ```

3. 다음은 이미지를 저장하는 것이다.

   ```python
   img.save("my-image.png")
   ```

4. 다음은 이미지의 배율을 조정하는 것이다.

   ```python
   thumbnail = img.scale(90,90)
   thumbnail.show()
   ```

5. 다음은 이미지에 `erode` 효과를 적용한 것이다.

   ```python
   eroded = img.erode()
   eroded.show()
   ```

6. 다음은 이미지의 특정 부분만 추출하는 것이다. 여기서는 x, y 좌표가 100, 100인 곳에서 넓이, 높이를 50, 50만큼 추출한다.

   ```python
   cropped = img.crop(100,100,50,50)
   cropped.show()
   ```

## 파이 카메라 연결하기

1. 다음을 입력해 `nano` 에디터를 실행한다.

   ```bash
   nano simplecv.py
   ```

2. 다음과 같이 입력한다.

   ```python
   from SimpleCV import *
   from picamera import PiCamera
   import time
   
   picam = PiCamera(resolution=(320,240), framerate=32)
   
   while True:
    picam.capture('/home/pi/cam.jpg', use_video_port=True)
    img = Image('/home/pi/cam.jpg')
    img.show()
   ```

3. 다음을 입력해 실행하면 파이 카메라 영상을 실시간으로 볼 수 있다.

   ```bash
   python simplecv.py
   ```

## 코너

1. 앞서 작업한 `simplecv.py`를 다음과 같이 수정한다.

   ```python
   from SimpleCV import *
   from picamera import PiCamera
   import time
   
   picam = PiCamera(resolution=(320,240), framerate=32)
   
   while True:
   picam.capture('/home/pi/cam.jpg', use_video_port=True)
   img = Image('/home/pi/cam.jpg')
   img.findCorners().show()
   ```

2. 다음을 입력해 실행하면 이미지에서 코너를 표시하는 것을 볼 수 있다.

   ```bash
   python simplecv.py
   ```

## Blob

1. 앞서 작업한 `simplecv.py`를 다음과 같이 수정하고 실행하면 이미지에서 **Blob**이 표시되는 것을 볼 수 있다.

   ```python
   from SimpleCV import *
   from picamera import PiCamera
   import time
   
   picam = PiCamera(resolution=(320,240), framerate=32)
   
   while True:
   picam.capture('/home/pi/cam.jpg', use_video_port=True)
   img = Image('/home/pi/cam.jpg')
   img.findBlobs().show()
   ```

2. 다시 다음과 같이 수정하고 실행하면 이미지에서 각 **Blob**이 서로 다른 색으로 표시되는 것을 볼 수 있다.

   ```python
   from SimpleCV import *
   from picamera import PiCamera
   import time
   
   picam = PiCamera(resolution=(320,240), framerate=32)
   
   while True:
   picam.capture('/home/pi/cam.jpg', use_video_port=True)
   img = Image('/home/pi/cam.jpg')
   img.findBlobs().show(autocolor=True)
   ```