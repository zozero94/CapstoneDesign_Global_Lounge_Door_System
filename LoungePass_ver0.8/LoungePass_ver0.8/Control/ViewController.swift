//
//  ViewController.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var pwTextField: UITextField!
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    @IBOutlet var autoLoginButton: UIButton!
    
    private var auto :Bool!
    private let control = MainViewControll()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        idTextField.setLeftPaddingPoints(35)
        pwTextField.setLeftPaddingPoints(35)
        activityIndicator.stopAnimating()
        auto = control.isAutoLogin()
        self.control.initBrightness(brightness: Float(UIScreen.main.brightness))
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        if auto { self.present()}
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    
    @IBAction func loginClick(_ sender: UIButton) {
        
        activityIndicator.startAnimating()
        
        DispatchQueue.global(qos: .userInitiated).async {
            let user = self.getUserInfo()
            
            let ispass = self.control.loginClicked(user: user)
            // indicator동안 실행할 코드
            
            print(ispass)
            
            // 실행 후 코드
            DispatchQueue.main.async {
                
                switch ispass {
                case 0: self.showAlert(Message: "잘못된 형식입니다.")
                case 1: self.showAlert(Message: "아이디를 확인해주세요.")
                case 2: self.showAlert(Message: "패스워드를 확인해주세요.")
                case 3: self.showAlert(Message: "네트워크를 확인해주세요.")
                case 4: self.showAlert(Message: "중복로그인 요청입니다.")
                case 5: self.showAlert(Message: "서버와 연결이 원활하지 않습니다.")
                case 6: self.showAlert(Message: "해당 데이터가 존재하지 않습니다.")
                case 7: self.present()
                default : break
                }
                self.activityIndicator.stopAnimating()
            }
            
        }
    }
    
    @IBAction func autoLoginClick(_ sender: UIButton) {
        
        if autoLoginButton.currentImage == UIImage(named: "자동로그인on") {
            autoLoginButton.setImage(UIImage(named: "자동로그인off"), for: .normal)
            self.control.autoLoginClicked(isAuto: false)
        }else {
            autoLoginButton.setImage(UIImage(named: "자동로그인on"), for: .normal)
            self.control.autoLoginClicked(isAuto: true)
            
        }
    }
    
    @IBAction func newIpClick(_ sender: UIButton) {
        
        self.control.setNewIP(newIP: idTextField.text!)
        self.showAlert(Message: "[IP변경]")
    }
    
    
    func getUserInfo()->UserInfo{
        let user = UserInfo()
        
        user.id = self.idTextField.text
        user.pw = self.pwTextField.text
        if user.id == "admin" && user.pw == "admin"{ user.tag = "2"}else {user.tag = "1"}
        
        return user
    }
    
    func present() {
        let storyboard: UIStoryboard = self.storyboard!
        let nextView = storyboard.instantiateViewController(withIdentifier: "pass")
        present(nextView, animated: true, completion: nil)
    }
    
    //알림창 띄우기
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        self.present(alert, animated: true)
    }
}

extension UITextField {
    func setLeftPaddingPoints(_ amount: CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}
