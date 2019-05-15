//
//  ViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController  {
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var pwTextField: UITextField!
    @IBOutlet var autoLoginButton: UIButton!
    @IBOutlet var loginButton: UIButton!
    
    //login
    var isAutoLogin : Bool = false
    var isLogOut: String = ""
    let rsa: RSAWrapper? = RSAWrapper()
    
    //json
    var dic : [String : String] = [:]
    var dataDic : [String : String] = [:]
    var dicToString: String? = ""
    var checkLogout : String = ""
    var jsonStringData : String? = ""
    var pubKey : String? = ""
    var prvKey : String? = ""

    var hexValue : String? = ""
    var modulusValue : String? = ""

    var client: ServerConnection = ServerConnection.sharedInstance
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        idTextField.setLeftPaddingPoints(30)
        pwTextField.setLeftPaddingPoints(30)
        
        print("viewDidLoad() 호출됨.//// -> isAutoLogin값 -> \(isAutoLogin)")
    }
    
    //화면 나타나기 직전
    override func viewWillAppear(_ animated: Bool) {
        checkLogout = String(UserDefaults.standard.string(forKey: "logout") ?? "0")
        
        if checkLogout == "0" {
            
        }else if checkLogout == "LOGOUT"{
            UserDefaults.standard.set(false, forKey: "autoLogin")
        } else {
            UserDefaults.standard.set(true, forKey: "autoLogin")
        }
        
        if UserDefaults.standard.bool(forKey: "autoLogin") == true {
            idTextField.text = UserDefaults.standard.string(forKey: "id")
            pwTextField.text = UserDefaults.standard.string(forKey: "pwd")
            loginBtn(loginButton)
        } else {
            idTextField.text = ""
            pwTextField.text = ""
            print("delete id, pwd")
            UserDefaults.standard.removeObject(forKey: "id")
            UserDefaults.standard.removeObject(forKey: "pwd")
            UserDefaults.standard.set(false, forKey: "autoLogin")
            UserDefaults.standard.synchronize()
        }
        print(UserDefaults.standard.bool(forKey: "autoLogin"))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
    }
    
    
    //자동로그인
    @IBAction func autoLogin(_ sender: UIButton) {
        let checkedImage: UIImage? = UIImage(named: "자동로그인on.png")
        let noncheckedImage: UIImage? = UIImage(named: "자동로그인off.png")
        
        sender.isSelected = !sender.isSelected
        if sender.isSelected == true {
            isAutoLogin = true
            autoLoginButton.setImage(checkedImage, for: UIControl.State.normal)
        } else {
            isAutoLogin = false
            autoLoginButton.setImage(noncheckedImage, for: UIControl.State.normal)
        }
    }
    
    //로그인 버튼 클릭 시
    @IBAction func loginBtn(_ sender: UIButton) {
        if idTextField.text == "" || pwTextField.text == "" {
            showAlert(Message: "아이디/패스워드를 입력해주세요.")
        } else {
            let success: Bool = (rsa?.generateKeyPair(keySize: 512, privateTag: "com.atarmkplant", publicTag: "com.atarmkplant"))!
            if !success {
                print("RSA Failed")
                return
            }
            
            //rsa
            print("getPublicKey()",(rsa?.getPublicKey().debugDescription)!)
            
            let start = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 111)
            let end = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.endIndex, offsetBy: -180)
            let hex = (rsa?.getPublicKey().debugDescription)![start..<end]
            
            hexValue! = String(hex)
            
            print("hex->\(hex)")
            
            let startIdx = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 144)
            let endIdx = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.endIndex, offsetBy: -24)
            let modulus = (rsa?.getPublicKey().debugDescription)![startIdx..<endIdx]
            
            modulusValue! = String(modulus)
            
            print("modulus->\(modulus)")
            
            //111-116 exponent, 144,272 modulus
            //String(data:(rsa?.getPrivateKey().debugDescription)!, encoding: .utf8)
            
            let id = idTextField.text!
            let encryption = rsa?.encryptBase64(text: id)
            print("encryption->\(encryption!)")
            let decryption = rsa?.decpryptBase64(encrpted: encryption!)
            print("decryption->\(decryption!)")
            pubKey = encryption!
            
            UserDefaults.standard.set("sendLoginInfo",forKey: "sendLoginInfo")
            requestPost(Id: idTextField.text!, Pw: pwTextField.text!)
        }
    }
    
    //자동로그인 기능
    func setAutoLogin() {
        if self.isAutoLogin {
            UserDefaults.standard.set(self.idTextField.text, forKey: "id")
            UserDefaults.standard.set(self.pwTextField.text, forKey: "pwd")
            UserDefaults.standard.set(self.isAutoLogin,forKey: "autoLogin")
            UserDefaults.standard.synchronize()
        } else {
            UserDefaults.standard.set(self.isAutoLogin, forKey: "autoLogin")
            UserDefaults.standard.synchronize()
        }
    }
    
    //알림창 띄우기
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        
        self.present(alert, animated: true)
    }
    
    //HTTP통신
    func requestPost(Id: String, Pw: String){
        // https://udream.sejong.ac.kr/main/loginPro.aspx?rUserid=14011038&rPW=wodud31&pro=1
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
                            //print("response->\(response!)")
                            
                            if str.contains("alert"){
                                self.showAlert(Message: "아이디/패스워드를 확인해주세요.")
                                
                            } else {
                                self.dic.updateValue("105", forKey: "seqType")
                                self.dataDic.updateValue(self.idTextField.text!, forKey: "id")
                                self.dataDic.updateValue(self.hexValue!, forKey: "exponent")
                                self.dataDic.updateValue(self.modulusValue!, forKey: "modulus")
                                
                                //여기에다가 관리자인지 아닌지 -- 수정해야됌
                                if !self.idTextField.text!.elementsEqual("admin"){
                                    self.dataDic.updateValue("0", forKey: "type")
                                } else {
                                    self.dataDic.updateValue("1", forKey: "type")
                                }
                                
                                
                                //admin은 우회인증 x
                                
                                do {
                                    let dataDicToJsonData = try JSONSerialization.data(withJSONObject: self.dataDic, options: [])
                                    self.dicToString = String(data:dataDicToJsonData, encoding: .utf8)!
                                   
                                    self.dic.updateValue(self.dicToString!,forKey: "data")
                                  
                                } catch let error as NSError {
                                    print(error)
                                }
                                
                                self.setAutoLogin()
                                
                                do {
                                    let jsonData = try JSONSerialization.data(withJSONObject: self.dic, options: [])
                                    //let decoded = try JSONSerialization.jsonObject(with: jsonData, options: [])
                                
                                    self.jsonStringData = String(data: jsonData, encoding: .ascii)
                                    self.jsonStringData = self.jsonStringData!
                                    //self.jsonStringData! = self.jsonStringData!.components(separatedBy: ["\\"]).joined()
                                    self.jsonStringData!.append("\n")
                                    print(self.jsonStringData!)
                            
                                } catch let error as NSError {
                                    print(error)
                                }
                                //서버 연결/연결x
                                //if self.client.isConnected() {
                                
                                    //print(self.client.sendData(data: self.jsonStringData!))
                                    self.setLogOnView()
                                
                                //} else {
                                //   self.showAlert(Message: "서버가 연결되지 않았습니다.")
                                //}
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
    //로그온된 화면 보여주기
    func setLogOnView(){
        UserDefaults.standard.set("LOGON",forKey: "logout")
        let logOn = self.storyboard?.instantiateViewController(withIdentifier: "logon")
        logOn?.modalTransitionStyle = UIModalTransitionStyle.flipHorizontal
        self.present(logOn!, animated: true, completion: nil)
    }
    
    func stringToDictionary() -> [String:String] {
        var dictionary : [String:String] = [:]
        
        return dictionary
    }
}

extension UITextField {
    func setLeftPaddingPoints(_ amount: CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: -2, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}
