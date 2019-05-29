//
//  ViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController, UITextFieldDelegate {
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var pwTextField: UITextField!
    @IBOutlet var autoLoginButton: UIButton!
    @IBOutlet var loginButton: UIButton!
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    
    //login
    var isAutoLogin : Bool = false
    var isLogOut: String = ""
    let rsa: RSAWrapper? = RSAWrapper()
    let convertor:Convertor? = Convertor()
    
    //json
    var dic : [String : String] = [:]
    var dataDic : [String : String] = [:]
    var dicToString: String? = ""
    
    //server
    var dicFromServer: [String : String] = [:]
    var getStringFromServer : String = ""
    
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
        idTextField.returnKeyType = .done
        pwTextField.returnKeyType = .done
        idTextField.delegate = self
        pwTextField.delegate = self
    
        self.activityIndicator.stopAnimating()
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
        
        if UserDefaults.standard.bool(forKey: "autoLogin") {
            idTextField.text = UserDefaults.standard.string(forKey: "id")!
            pwTextField.text = UserDefaults.standard.string(forKey: "pwd")!
            
            loginBtn(loginButton)
            
        } else {
            idTextField.text = ""
            pwTextField.text = ""
            UserDefaults.standard.removeObject(forKey: "id")
            UserDefaults.standard.removeObject(forKey: "pwd")
            UserDefaults.standard.set(false, forKey: "autoLogin")
            UserDefaults.standard.synchronize()
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField.isEqual(idTextField){
            pwTextField.becomeFirstResponder()
        } else if textField.isEqual(pwTextField){
            pwTextField.resignFirstResponder()
        }
        return true
    }
    
    
    //자동로그인
    @IBAction func autoLogin(_ sender: UIButton) {
        let checkedImage: UIImage? = UIImage(named: "자동로그인on.png")
        let noncheckedImage: UIImage? = UIImage(named: "자동로그인off.png")
        
        sender.isSelected = !sender.isSelected
        
        if sender.isSelected == true {
            isAutoLogin = true
            UserDefaults.standard.set(self.isAutoLogin,forKey: "autoLogin")
            print(UserDefaults.standard.bool(forKey: "autoLogin"))
            autoLoginButton.setImage(checkedImage, for: UIControl.State.normal)
            
        } else {
            isAutoLogin = false
            UserDefaults.standard.set(self.isAutoLogin,forKey: "autoLogin")
            print(UserDefaults.standard.bool(forKey: "autoLogin"))
            autoLoginButton.setImage(noncheckedImage, for: UIControl.State.normal)
        }
    }
    
    //로그인 버튼 클릭 시
    @IBAction func loginBtn(_ sender: UIButton) {
        print("loginBtn")
        
        if idTextField.text! == "" || pwTextField.text! == "" {
            showAlert(Message: "아이디/패스워드를 입력해주세요.")
        }else if idTextField.text!.lengthOfBytes(using: String.Encoding.utf8) >= 20 {
            showAlert(Message: "올바른 아이디 형식이 아닙니다.")
        } else {
            let success: Bool = (rsa?.generateKeyPair(keySize: 512, privateTag: "com.atarmkplant", publicTag: "com.atarmkplant"))!
            if !success {
                print("RSA Failed")
                return
            }
            
            //rsa
            let start = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 111)
            let end = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 116)
            let hex = (rsa?.getPublicKey().debugDescription)![start..<end]
            
            hexValue! = String(hex)
            
            let startIdx = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 144)
            let endIdx = (rsa?.getPublicKey().debugDescription)!.index((rsa?.getPublicKey().debugDescription)!.startIndex, offsetBy: 272)
            let modulus = (rsa?.getPublicKey().debugDescription)![startIdx..<endIdx]
            
            modulusValue! = String(modulus)
    
            let id = idTextField.text!
            let encryption = rsa?.encryptBase64(text: id)

            pubKey = encryption!
            
            UserDefaults.standard.set(self.idTextField.text, forKey: "id")
            UserDefaults.standard.set(self.pwTextField.text, forKey: "pwd")
            UserDefaults.standard.set(self.isAutoLogin,forKey: "autoLogin")
            UserDefaults.standard.synchronize()
            
            if (self.idTextField.text!) == "admin" && (self.pwTextField.text!) == "admin"{
                self.dataDic.updateValue("2", forKey: "type")
                loginSequence()
                
            } else {
                self.dataDic.updateValue("1", forKey: "type")
                requestPost(Id: idTextField.text!, Pw: pwTextField.text!)
            }
        }
    }
    
    //자동로그인 기능
    func setAutoLogin() {
        if self.isAutoLogin {
            UserDefaults.standard.set(self.idTextField.text!, forKey: "id")
            UserDefaults.standard.set(self.pwTextField.text!, forKey: "pwd")
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
    
    //로그온된 화면 보여주기
    func setLogOnView(){
        UserDefaults.standard.set("LOGON",forKey: "logout")
        let logOn = self.storyboard?.instantiateViewController(withIdentifier: "logon")
        logOn?.modalTransitionStyle = UIModalTransitionStyle.flipHorizontal
        self.present(logOn!, animated: true, completion: nil)
    }
    
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
        
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: self.dic, options: [])
            
            self.jsonStringData = String(data: jsonData, encoding: .ascii)
            self.jsonStringData = self.jsonStringData!
            self.jsonStringData!.append("\n")
            
        } catch let error as NSError {
            print(error)
        }
        
        //서버 연결/연결x
        self.getStringFromServer = self.client.sendData(data: self.jsonStringData!)
        
        self.dicFromServer = self.convertor!.convertStringToDic(jsonString: self.getStringFromServer)
        
        self.activityIndicator.startAnimating()
        
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
                self.activityIndicator.stopAnimating()
            }
        }
    }
}

extension UITextField {
    func setLeftPaddingPoints(_ amount: CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: -2, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}
