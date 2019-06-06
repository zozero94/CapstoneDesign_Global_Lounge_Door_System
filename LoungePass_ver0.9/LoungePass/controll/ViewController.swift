//
//  ViewController.swift
//  LoungePass
//
//  Created by MacBook on 31/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var pwTextField: UITextField!
    @IBOutlet var autoLoginButton: UIButton!
    @IBOutlet var LoginButton: UIButton!
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    
    private let control = MainControl()
    private let user = User.sharedInstance
    private var autologin = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        idTextField.setLeftPaddingPoints(35)
        pwTextField.setLeftPaddingPoints(35)
        activityIndicator.stopAnimating()

        UserDefaults.standard.set(UIScreen.main.brightness, forKey: "brightness")
        UserDefaults.standard.synchronize()
        // 자동로그인 확인
        autologin = control.isAutoLogin()
       
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if autologin { self.present()}
    }
    
    // 화면 터치시 키보드 내리기
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    
    @IBAction func autoLoginClick(_ sender: UIButton) {
      
        if autoLoginButton.currentImage == UIImage(named: "자동로그인on"){
            autoLoginButton.setImage(UIImage(named: "자동로그인off"), for: .normal)
            control.autoLoginClicked(auto: false)
        }else {
            autoLoginButton.setImage(UIImage(named: "자동로그인on"), for: .normal)
            control.autoLoginClicked(auto: true)
        }
        
    }
    
    @IBAction func loginClick(_ sender: UIButton) {
        
        activityIndicator.startAnimating()
        
        control.setUserLoginInfo(id: idTextField.text!, pw: pwTextField.text!)
        
        DispatchQueue.global(qos: .userInitiated).async {

             // indicator동안 실행할 코드
            
            let pass = self.control.loginClicked()

            // 실행 후 코드
            DispatchQueue.main.async {
                
                switch pass {
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

    
    //화면 전환
    func present() {
        let storyboard: UIStoryboard = self.storyboard!
        let nextView = storyboard.instantiateViewController(withIdentifier: "Pass")
        present(nextView, animated: true, completion: nil)
    }
    
    //알림창 띄우기
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        self.present(alert, animated: true)
    }
    

}

// 텍스트필드 세팅
extension UITextField {
    func setLeftPaddingPoints(_ amount: CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}


