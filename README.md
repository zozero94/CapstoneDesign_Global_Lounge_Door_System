# CapstoneDesign-Global-Lounge-Door-System-

## Capston Project 

세종대학교 컴퓨터공학과 14011262 임소율

---

##### 세종대학교 글로벌라운지 출입 시스템 -> 출입증(Ios)

#### 1. 개요 

교내 Global Lounge의 출입을 위해 QR코드를 이용한 출입증

#### 2. How

* 학교 서버로 학생인증 정보를 받아옴. (학생)
* 자체 서버에 인증요청 및 개인정보를 받아옴. (관리자 및 학생)
* QR코드 
* 비콘을 이용한 출입 요청. (관리자)

---

#### 3. 기능

**1) 자동 로그인 기능**

* ID 와 Password 를 `UserDefault` 내에 저장.

**2)로그인 시 자동 밝기 조절**

* 큐알코드 인식이 바로 될 수 있도록 출입증 화면으로 넘어가면 밝기를 키워줌.
* 앱 구동시 `UserDefault` 내에 밝기 정보 저장.
* 출입증 화면으로 넘어간 후 ` UIScreen.main.brightness` 값 최대

**3) 학교 서버와 HTTP 통신** 

**4) 서버와 소켓 통신** 

**5) 개인정보 보호를 위한 RSA 암호화 (Base64)** 

* JAVA(서버) 와 키 포맷이 달라 Swift내에서 만든 공개키를 넘겨주면 서버 내에서 암호화 진행이 불가능 하게 됨. 따라서 공개키 정보 중 ` Exponent` 값과 ` Hex `을 찾아 서버로 넘겨줌.

* 암호화하려는 대상의 길이가 한번에 암호화 할 수 있는 길이보다 길어 `52Byte`씩 잘라서 암호화 후 붙이는 과정으로 암호화 진행.(서버)

  —> 앱 에서는 `64Byte`씩 나누어서 복호화 진행. (12Byte 는 padding)

**6) 출입 인증시 QR코드 기존 QR코드 만료 후 자동으로 생성**

* 출입 인증에 성공하게 되면 서버에서 QR 코드를 만료후 재 생성하여 정보를 넘겨줌. 따라서 socket의 read기능을 하는 스레드를 두어 QR코드 정보를 넘겨줄때까지 대기. 
* 비동기 콜백 구조 main queue -> global queue -> main queue 

~~~swift
DispatchQueue.global(qos: .userInitiated).async {
     // 소켓 response 대기  
   while (self.flag == true) {
                
      let response = self.control.isResponse()
      if response == nil { continue }
                
      // response 후 데이터에따라 뷰에 뿌려주기
      DispatchQueue.main.async {
                    
        switch response!["seqType"] as! String {
        case ServerConstant().STATE_CREATE : self.qrImageView.image = response!["qr"] as? UIImage
        case ServerConstant().STATE_URL : self.userImageView.image = response!["img"] as? UIImage
        case ServerConstant().STATE_NO : self.showAlert(Message: "출입문을 열수없습니다.")
        default: break
        }
      }
   } // while
}// global.async
~~~

 **7) 비콘**

* 비콘영역내에서 *shake gesture*를 취하면 관리자 에게 출입문 개방을 요청.
* 서버 컴퓨터 앞에 없어도 외부인이 출입을 요청 할 경우 출입문을 열어줄 수 있음.
* (확장성) 지금은 관리자에게만 비콘 기능을 제공하지만 추후에 일반 유저에게도 제공하게 되면 휴대폰 액정이 손상된 경우나 시각장애인등의 출입을 용이하게 할 수 있음.

---

#### 4. Implementation	

**1) RSA**

RSA는 대표적인 공개키 암호로서 Diffie와 Hellman의 공개키 암호 개념을 기반으로 MIT공대 연구팀 소속의 세 학자 Rivest, Shamir, Adleman에 의해 탄생되었고, RSA이름은 세 학자 이름의 머리글자를 따서 만든 명칭이다.

RSA는 큰 수의 **소인수분해가 매우 어렵다는 것을 기반으로 만들어짐.** n = p*q일 때,  p와 q로 n을 구하기는 쉬우나  n으로 p와  q를 찾기 힘들다는 소인수분해의 어려움을 이용하였음.

* RSA 표기법

  | p, q       | 매우 큰 서로 다른 소수                                       |
  | ---------- | ------------------------------------------------------------ |
  | n          | p*q의 합성수                                                 |
  | gcd(a, b)  | a와b의 최대 공약수                                           |
  | φ(n)       | 오일러 Totient함수로, φ(n)은 n보다 작은 자연수 중에서 n과 서로 소인 자연수의 개수 |
  | a mod n    | 모듈러 연산으로, a를 n로 나누었을 때 나머지 값               |
  | a≡ r mod n | a와 r은 n으로 나누었을 때 그 나머지가 같음 (a와 r은 합동)    |
  | e          | n과 함께 공개되는 공개키로 암호화 지수에 사용                |
  | d          | 공개되지 않는 개인키로 복호화 지수에 사용                    |
  | M          | 평문으로 공개키를 이용하여 암호화                            |
  | C          | 암호문으로 개인키를 이용하여 복호화                          |
  | KU         | 공개키로 KU= {e, n}으로 표기                                 |
  | KR         | 개인키로 KR= {d, n}으로 표기                                 |

* 키 생성 알고리즘

  1)    서로 다른 큰 소수 p와 q를 선택한다.

  2)    n= p*q를 계산한다.

  3)    φ(n)=(p-1)(q-1)를 계산한다. 

  4)    φ(n)보다 작고 φ(n)과 서로소인 임의의 자연수 e를 선택한다. (gcd(e, φ(n))=1, 1 < e < φ(n)에 만족하는 e 선택) 

  5)    확장 유클리드 호제법을 이용하여 e mod φ(n)에 대한 곱의 역원, 즉 ed mod φ(n)=1인 d를 구한다. 
        (ed≡ 1 mod 		φ(n), 1 < d < φ(n)에 만족하는 d 값)

  6)    공개키: KU= {e, n} 

  7)    개인키: KR= {d, n}

* 암호화/복호화

  * 암호화 : M < n C≡ Me mod n
  * 복호화 : M≡ Cd mod n

* 예시 

  ![image](https://user-images.githubusercontent.com/48287388/58787137-22111400-8624-11e9-9988-756933bdb992.png)

*참고자료* 

> [**RSA Encryption**](https://www.nexg.net/rsa-%EC%95%94%ED%98%B8%ED%99%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98%EC%9D%98-%EC%9D%B4%ED%95%B4/)

 **2) 비콘**

비콘이 전송하는 신호에는 UUID, minor, major 값이 담겨있음. 비콘과 디바이스는 양방향 통신이 아닌 단방향으로 어떤 정보를 전송하는 것이 아닌 단순히 신호만을 전송함. 해당 신호에 대한 정보를 서버에 저장해두고 이를 바탕으로 신호가 감지되었을 때 알맞게 처리한다.

iOS Core Location 프레임워크를 활용하여 비콘의 영역을 등록하고 이를 추적. 기본적으로 iOS는 최대 20개의 영역까지만 등록할 수 있음. 그렇기 때문에 비콘 각각을 영역으로 삼지 않고 코엑스, 스타필드와 같은 복합 시설 전체를 하나의 영역으로 설정

그렇다면 스타필드 내의 모든 매장은 같은 영역 값을 갖고 이는 비콘의 UUID에 해당, 즉 한 스타필드 내의 모든 매장의 UUID는 동일. 각 매장을 구분하는 것은 비콘의 minor 값과 major 값으로 식별.

비콘의 신호를 감지는 두 단계에 걸쳐서 이루어짐

- Monitoring

  \- 비콘의 영역을 판단하는 행위로 디바이스 주변의 비콘 영역을 스캔한다. 그리고 비콘 영역의 내/외부를 판단함. 비콘의 영역안에 들어온 순간과 나가는 순간에 행동을 정의해줄 수 있음

  - 영역의 모니터링은 Core Location을 활용함. 영역 모니터링을 위해서는 앱의 사용과 상관없이 위치 서비스를 항상 사용하기 위한 권한 요청이 필요함.
  - 또한 백그라운드에서 위치의 갱신도 이루어져야 하기 때문에 해당 기능도 프로젝트 내에서 설정이 선행되어야 함.
  - 관련 메소드 및 코드
    - `func startMonitoring(for region: CLRegion)` - 원하는 영역을 Monitoring
    - `locationManager(_ manager: CLLocationManager, didEnterRegion region: CLRegion)` - 영역 안에 들어온 순간에 호출되는 메소드
    - `func locationManager(_ manager: CLLocationManager, didExitRegion region: CLRegion)` - 영역을 벗어난 순간에 호출되는 메소드
    - `func locationManager(_ manager: CLLocationManager, didDetermineState state: CLRegionState, for region: CLRegion)` - 영역의 내부인지 외부인지를 판단하는 메소드, 모니터링을 시작한 순간에 이미 영역 안이라면 위의 `didEnterRegion` 은 호출되지 않고 `didDetermineState`가 먼저 호출됨. `state`가 `.inside`면 Ranging을 시작하고 `.outside`가 되면 Ranging을 멈추는 코드를 이 메소드안에 구현

- Ranging 

  \- 비콘의 영역 안에서 비콘과의 근접도를 측정하는 행위.

  - 이 행위는 위치에 따른 근접도가 아닌 감지되는 신호의 세기에 따라 달라지기 때문에 거리에 대한 정보를 사용자에게 제공해주는 것은 옳지 않음. 실제로 신호의 세기에 따라 더 가까운 비콘이 더 멀다고 측정되는 경우가 간간히 있음.
  - 관련 메소드 및 코드
    - `func locationManager(_ manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], in region: CLBeaconRegion)` - Ranging하고 있는 비콘들을 매개변수로 받기 때문에 이를 활용하여 코드를 작성할 수 있음.
    - `beacons.first `를 통해 가장 가까운 비콘에 대한 행위를 정의해줄 수 있다.
    - `nearestBeacon.proximity `을 통해 비콘과의 근접도를 판단할 수 있다. `.immediate`, `.near` , `.far` 순으로 근접한 근접도를 판단함.

  

  *참고자료*

  > [**Determining the Proximity to an iBeacon**](https://developer.apple.com/documentation/corelocation/determining_the_proximity_to_an_ibeacon)
  >
  > [**CLBeaconRegion**](https://developer.apple.com/documentation/corelocation/clbeaconregion)
  >
  > [**BeaconSolution**](https://github.com/ehdrjsdlzzzz)

---

##### < 관련화면 >

1) 기본 과정

![image](https://user-images.githubusercontent.com/48287388/58760693-7bbb0500-8576-11e9-82a9-25aacbfdd546.png)

2) 예외처리

![image](https://user-images.githubusercontent.com/48287388/58785777-3273bf80-8621-11e9-8206-33172c6bfb74.png)
