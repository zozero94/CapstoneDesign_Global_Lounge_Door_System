//
//  ViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController {
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var pwTextField: UITextField!
    @IBOutlet var autoLoginButton: UIButton!
    @IBOutlet var loginButton: UIButton!
    
    var isAutoLogin : Bool = false
    var isLogOut: String = ""
    var dic : [String : String] = [:]
    var checkLogout : String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        idTextField.setLeftPaddingPoints(30)
        pwTextField.setLeftPaddingPoints(30)
        
        
        print("viewDidLoad() 호출됨.//// -> isAutoLogin값 -> \(isAutoLogin)")
    }
    
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
        print("viewWillAppear() 호출됨.")
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
            generateKeys(text: idTextField.text!)
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
                            print("response->\(response!)")
                            
                            if str.contains("alert"){
                                self.showAlert(Message: "아이디/패스워드를 확인해주세요.")
                            } else {
                                self.dic.updateValue(self.idTextField.text!, forKey: "id")
                                self.dic.updateValue("100", forKey: "flag")
                                self.setAutoLogin()
                                print("LOGIN-> \(self.dic["flag"]!)) / id -> \(self.dic["id"]!) /publicKey -> \(self.dic["publicKey"]!)")
                                
                                do {
                                    let jsonData = try JSONSerialization.data(withJSONObject: self.dic, options: JSONSerialization.WritingOptions.prettyPrinted)
                                } catch let error as NSError {
                                    print(error)
                                }
                                
                                self.setLogOnView()
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
    //JSON -> String
    func jsonToString(json: AnyObject) -> String{
        var data : String = ""
        
        do {
            let data1 = try JSONSerialization.data(withJSONObject: json, options: JSONSerialization.WritingOptions.prettyPrinted)
            let convertedString = String(data: data1, encoding: String.Encoding.utf8)
            
            data = convertedString!
        } catch let error as NSError {
            print(error)
        }
        return data
    }
    
    //RSA
    func generateKeys(text: String) {
        var publicKey: SecKey?
        var privateKey: SecKey?
        
        let publicKeyAttr: [NSObject:NSObject] = [kSecAttrIsPermanent: true as NSObject, kSecAttrApplicationTag: "publicTag".data(using: String.Encoding.utf8)! as NSObject]
        let privateKeyAttr: [NSObject:NSObject] = [kSecAttrIsPermanent: true as NSObject, kSecAttrApplicationTag: "privateTag".data(using: String.Encoding.utf8)! as NSObject]
        
        var keyPairAttr = [NSObject:NSObject]()
        keyPairAttr[kSecAttrKeyType] = kSecAttrKeyTypeRSA
        keyPairAttr[kSecAttrKeySizeInBits] = 512 as NSObject      //크기 조정
        keyPairAttr[kSecPublicKeyAttrs] = publicKeyAttr as NSObject
        keyPairAttr[kSecPrivateKeyAttrs] = privateKeyAttr as NSObject
        
        _ = SecKeyGeneratePair(keyPairAttr as CFDictionary, &publicKey, &privateKey)
        
        var error:Unmanaged<CFError>?
        if #available(iOS 10.0, *) {
            if let cfdata = SecKeyCopyExternalRepresentation(publicKey!, &error) {
                let data:Data = cfdata as Data  //data
                //let data:Data? = textField.text!.data(using: .utf8) //textField
                let b64Key = data.base64EncodedString(options: .lineLength64Characters)
                //let b64Key = data!.base64EncodedString(options: .lineLength64Characters)  //textField
                print("public base 64 : \n\(b64Key)")
            }
            if let cfdata = SecKeyCopyExternalRepresentation(privateKey!, &error) {
                let data:Data = cfdata as Data
                let b64Key = data.base64EncodedString(options: .lineLength64Characters)
                print("private base 64 : \n\(b64Key)")
            }
        }
        
        let encrypted = encryptBase64(text: text, key: publicKey!)
        let decrypted = decryptBase64(encrypted: encrypted, key: privateKey!)
        
        self.dic.updateValue(encrypted, forKey: "publicKey")
        
        print("decrypted \(String(describing: decrypted))")
        
        self.dismiss(animated: true, completion: nil)
    }
    
    func encryptBase64(text: String, key:SecKey) -> String {
        let plainBuffer = [UInt8](text.utf8)
        var cipherBufferSize : Int = Int(SecKeyGetBlockSize(key))
        var cipherBuffer = [UInt8](repeating: 0, count:Int(cipherBufferSize))
        
        let status = SecKeyEncrypt(key, SecPadding.PKCS1, plainBuffer, plainBuffer.count, &cipherBuffer, &cipherBufferSize)
        
        if status != errSecSuccess {
            print("Failed Encryption")
        }
        
        let mudata = NSData(bytes: &cipherBuffer, length: cipherBufferSize)
        
        return mudata.base64EncodedString()
    }
    
    func decryptBase64(encrypted: String, key: SecKey) -> String? {
        let data : NSData = NSData(base64Encoded: encrypted, options: .ignoreUnknownCharacters)!
        let count = data.length / MemoryLayout<UInt8>.size
        var array = [UInt8](repeating: 0, count: count)
        data.getBytes(&array, length: count*MemoryLayout<UInt8>.size)
        
        var plaintextBufferSize = Int(SecKeyGetBlockSize(key))
        var plaintextBuffer = [UInt8](repeating: 0, count: Int(plaintextBufferSize))
        
        let status = SecKeyDecrypt(key, SecPadding.PKCS1, array, plaintextBufferSize, &plaintextBuffer, &plaintextBufferSize)
        
        if status != errSecSuccess {
            print("Failed Decrypt")
            return nil
        }
        return NSString(bytes: &plaintextBuffer, length: plaintextBufferSize, encoding: String.Encoding.utf8.rawValue)! as String
    }
}

extension UITextField {
    func setLeftPaddingPoints(_ amount: CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: -2, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}

