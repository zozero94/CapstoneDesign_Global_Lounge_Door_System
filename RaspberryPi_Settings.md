# 라즈베리 파이 설정하기

 라즈베리 파이를 사용하기 위한 기본 초기설정, WiFi, SSH, 백업/복구 방법 

## 준비물

- 라즈베리 파이 3
- 5V 2A 어댑터
  **(USB를 통한 전원 공급이 가능하나, GPIO를 사용하는 경우 전원 공급의 문제가 발생할 수 있음으로 가급적 5V 2A 어댑터 사용을 추천한다.)**
- MicroSD 카드 16GB
- USB 마우스, 키보드
- HDMI 모니터
- 파이 카메라



## 초기설정

Raspbian의 경우 설치한 뒤 번거롭지만 초기설정을 해줘야한다. 물론 이것도 처음만 번거롭지 한번 해놓으면 다시 바꿀 일이 거의 없다.

1. Raspbian의 상단의 툴바에 **Terminal** 아이콘을 클릭한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00000.jpg)
2. `sudo raspi-config`를 입력한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00002.jpg)
3. **Internationalisation Options**를 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00003.jpg)
4. **Change Locale**을 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00004.jpg)
5. **ko_KR.UTF-8 UTF-8**을 스페이스바를 눌러 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00005.jpg)
6. **ko_KR.UTF-8**을 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00006.jpg)
7. **Internationalisation Options**를 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00007.jpg)
8. **Change Timezone**을 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00008.jpg)
9. **Asia**를 선택한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00009.jpg)
10. Seoul을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00010.jpg)
11. **Internationalisation Options**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00011.jpg)
12. **Change Keyboard Layout**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00012.jpg)
13. **Generic 105-key (Intl) PC**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00013.jpg)
14. **Other**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00014.jpg)
15. **Korean**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00015.jpg)
16. **Korean - Korean (101/104 key compatible)**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00016.jpg)
17. **The default for the keyboard layout**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00017.jpg)
18. **No compose key**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00018.jpg)
19. **No**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00019.jpg)
20. **Advanced Options**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00020.jpg)
21. **Overscan**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00021.jpg)
22. **Disable**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00022.jpg)
23. **Advanced Options**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00023.jpg)
24. **Audio**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00024.jpg)
25. **Force HDMI**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00025.jpg)
26. **Advanced Options**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00026.jpg)
27. **SSH**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00027.jpg)
28. **Enable**을 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00028.jpg)
29. **Ok**를 누른다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00029.jpg)
30. **Finish**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00030.jpg)
31. **Yes**를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00031.jpg)
32. 재시작하면 글자가 깨진 것을 볼 수 있다. 한글을 지원하는 글자체가 없기 때문이다. 이를 위해 한글을 지원하는 글자체를 설치한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00032.jpg)
33. 툴바에서 아래 표시한 아이콘을 클릭한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00033.jpg)
34. WiFi 목록이 표시되면 사용할 WiFi를 선택한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00034.jpg)
35. WiFi 비밀번호를 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00035.jpg)
36. 터미널을 실행한 뒤 `sudo apt-get update`를 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00036.jpg)
37. `sudo apt-get upgrade`를 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00037.jpg)
38. 실행하면 계속 진행할거냐고 묻는다. **Y**를 눌러 계속 진행한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00038.jpg)
39. `sudo apt-get install ttf-unfonts-core ibus ibus-hangul`을 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00039.jpg)
40. 실행하면 계속 진행할거냐고 묻는다. **Y**를 눌러 계속 진행한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00040.jpg)
41. `sudo rpi-update`를 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00041.jpg)
42. `sudo reboot`을 입력한다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00042.jpg)
43. 재시작하면 한글이 정상적으로 표시되는 것을 볼 수 있다.
    ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/default-setting-00043.jpg)

## SSH

SSH를 이용해 라즈베리 파이에 접속할 수 있다.

1. 터미널에서 `ifconfig`를 입력한다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/ssh-00.jpg)
2. 다음과 같이 라즈베리 파이의 IP 주소를 알 수 있다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/ssh-01.jpg)
3. SSH 프로그램을 이용해 라즈베리 파이로 접속한다. 기본 아이디와 패스워드는 다음과 같다.

| 아이디 | 패스워드  |
| ------ | --------- |
| pi     | raspberry |

## 백업 및 복구

라즈베리 파이 운영체제를 백업하고 복구하는 방법을 배운다.

### Windows

1. **Win32 Disk Imager**(<https://goo.gl/TL5a1X>)를 다운로드해 설치한다.
2. **Image File**의 경로를 지정하고, **Read**를 누르면 라즈베리 파이 MicroSD 카드를 파일로 저장한다. 반대로 복구하고 싶은 경우에는 복구하고 싶은 이미지 파일을 선택하고 **Write**를 누르면 라즈베리 파이 MicroSD 카드에 이미지 파일의 내용이 반영된다.
   ![img](https://neosarchizo.gitbooks.io/raspberrypiforsejonguniv/content/images/chapter1/backup-windows-00.jpg)

### Mac

1. `diskutil list`을 입력해 MicroSD 카드의 볼륨을 확인한다. disk#와 같은 형태로 되어있다.
2. `diskutil unmountDisk /dev/disk#`를 입력한다.
3. 백업을 할때는 `sudo dd bs=1m if=/dev/rdisk# of=backup.img`을 입력한다.
4. 복구를 할때는 `sudo dd bs=1m if=backup.img of=/dev/rdisk#`을 입력한다.

**Mac의 경우 진행 중인게 표시되지 않는다. 따라서 오래 걸린다고 중간에 중단시키면 안 된다.**