# CapstoneDesign-Global-Rounge-Door-System-

### Singleton Pattern 으로 구현

<hr/>

### 로그인 화면

  #### 1-1) 학생 우회 인증
  ```Swift
  func requestPost(Id: String, Pw: String){
        let strUrl : String = "https://udream.sejong.ac.kr/main/loginPro.aspx" + "?rUserid=" + Id + "&rPW=" + Pw + "&pro=1"
        let url = URL(string: strUrl)
        
        if let _url = url {
            var request = URLRequest(url: _url)
            request.httpMethod = "POST"
            
            let session = URLSession.shared
            let task = session.dataTask(with: request, completionHandler: {(data: Data?, response: URLResponse?, error:Error?) in
                guard error == nil && data != nil else {
                    if let err = error {
                        print(err.localizedDescription)
                    }
                    return
                }
                if let _data = data {
                    if let strData = NSString(data: _data, encoding: String.Encoding.utf8.rawValue){
                        let str = String(strData)
                        
                        DispatchQueue.main.async {
                            if str.contains("alert"){
                                self.showAlert(Message: "아이디/패스워드를 확인해주세요.")
                            } else {
                                self.loginSequence()
                            }
                        }
                    }
                } else {
                    print("data nil")
                }
            })
            task.resume()
        }
    }
   ```
    
  #### 1-2) 서버 인증
```Swift
    func loginSequence() {
        self.dic.updateValue("104", forKey: "seqType")
        self.dataDic.updateValue(self.idTextField.text!, forKey: "id")
        self.dataDic.updateValue(self.hexValue!, forKey: "exponent")
        self.dataDic.updateValue(self.modulusValue!, forKey: "modulus")
        
        do {
            let dataDicToJsonData = try JSONSerialization.data(withJSONObject: self.dataDic, options: [])
            self.dicToString = String(data:dataDicToJsonData, encoding: .utf8)!
            
            self.dic.updateValue(self.dicToString!,forKey: "data")
        } catch let error as NSError {
            print(error)
        }
        
        //서버 연결/연결x
        self.getStringFromServer = self.client.sendData(data: self.jsonStringData!)
        
        self.dicFromServer = self.convertor!.convertStringToDic(jsonString: self.getStringFromServer)
       
        DispatchQueue.global(qos: .userInitiated).async {
            DispatchQueue.main.async {
                if self.dicFromServer["seqType"] == "101"{
                    let decryption = self.rsa?.decryptBase64(encrpted: self.dicFromServer["data"]!)
                    let qrData = decryption!
                    UserDefaults.standard.set(qrData,forKey: "qrData")
                    
                    self.setLogOnView()
                        
            
                } else if self.dicFromServer["seqType"] == "102" {
                    self.showAlert(Message: "이미 로그인 중인 사용자입니다.")
                } else if self.dicFromServer["seqType"] == "103" {
                    self.showAlert(Message: "등록되지 않은 사용자입니다.")
                } else {
                    self.showAlert(Message: "서버가 연결되지 않았습니다.")
                }
            }
        }
    }
```
    
   
   <hr/>
    
### 2. 로그인 후 화면
   ### 2-1) QR코드 생성하기 
```Swift
    func generateQRCode(from string: String) -> UIImage?{
        let data = string.data(using: String.Encoding.utf8)
        
        if let filter = CIFilter(name: "CIQRCodeGenerator"){
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 2, y: 2)
        
            if let output = filter.outputImage?.transformed(by: transform){
                return UIImage(ciImage: output)
            }
        }
        return nil
    } 
```
     
     
    
   <hr/>
    
   #### +RSA 암호화
   #### +비콘(추가 예정)
   
   <hr/>
   
   ### 사용된 라이브러리
   #### -SwiftSocket
   #### -CoreLocation
   #### -UIKit
    
