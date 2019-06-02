//
//  StudentViewController.swift
//  LoungePass
//
//  Created by MacBook on 31/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import UIKit
import CoreLocation
import AudioToolbox


class PassViewController: UIViewController {
    
    @IBOutlet var qrImageBorder: UIImageView!
    @IBOutlet var qrImageView: UIImageView!
    @IBOutlet var userImageBorder: UIImageView!
    @IBOutlet var userImageView: UIImageView!
    @IBOutlet var nameTextField: UITextField!
    @IBOutlet var idTextField: UITextField!
    @IBOutlet var departmentTextField: UITextField!
    @IBOutlet var collegeTextField: UITextField!
    
    var loc = -1
    var locationManager:CLLocationManager!
    var flag = true
    
    private let imageGenerator = ImageGenerator()
    private let control = PassControl()
    private let user = User.sharedInstance
    let responseQueue = DispatchQueue(label: "response", qos: .userInitiated)
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        //1. 화면 밝기 최대
        UIScreen.main.brightness = CGFloat(1.0)
        
        //2. 관리자 비콘 start
        if control.user.tag  == "2"{
            locationManager = CLLocationManager()
            locationManager.delegate = self
            locationManager.requestAlwaysAuthorization()
        }
        
        //3. 이미지 border setting
        setImageView(image: qrImageBorder)
        setImageView(image: userImageBorder)
        
        // 4. 텍스트 세팅
        self.setTextField()
        
        // 5. 캡쳐방지 셋팅
        self.screenshotTaken()
        
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
        
    }// viewDidLoad()
    
    override func viewWillAppear(_ animated: Bool) {
    
        _ = self.control.requestData(key: "qr")
        _ = self.control.requestData(key: "img")
        
    }
    @IBAction func logoutClick(_ sender: UIButton) {
        // 1. 화면 밝기 원래대로
        print(UserDefaults.standard.float(forKey: "brightness"))
        UIScreen.main.brightness = CGFloat(UserDefaults.standard.float(forKey: "brightness"))
        self.flag = false
        self.control.logoutClicked()
        present()
        
    }
    
    // 캡쳐 확인
    func screenshotTaken(){
        NotificationCenter.default.addObserver(forName: UIApplication.userDidTakeScreenshotNotification, object: nil, queue: OperationQueue.main){ notification in
            self.showAlert(Message: "캡처본은 사용할 수 없습니다.")
            _ = self.control.requestData(key:"expiration")
            _ = self.control.requestData(key: "qr")
        }
    }
    // 흔들림감지.
    override func motionBegan(_ motion: UIEvent.EventSubtype, with event: UIEvent?) {
        
        if(user.tag == "2"){
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
            print(loc)
            if loc > 0 {
                let _ = control.requestData(key: "beacon")
            }
            else {
                self.showAlert(Message: "출입문과의 거리 인식이 불안정합니다.")
            }
        }
    }
    
    func setTextField() {
        
        if(user.tag == "1"){
            idTextField.text = "아이디 : " + user.id!
            nameTextField.text = "이름 : " + user.name!
            departmentTextField.text = "학과 : " + user.department!
            collegeTextField.text = "단과대학 : " + user.college!
        }
        else{
            
            idTextField.text = "아이디 : " + user.id!
            departmentTextField.text = "이름 : " + user.name!
            nameTextField.borderStyle = UITextField.BorderStyle.none
            collegeTextField.borderStyle = UITextField.BorderStyle.none
        }
    }
    
    // 이미지 border 
    func setImageView(image: UIImageView) {
        image.layer.borderColor = UIColor.black.cgColor
        image.layer.borderWidth = 0.5
        image.layer.cornerRadius = 10
    }
    
    // 화면 전환
    func present() {
        let storyboard: UIStoryboard = self.storyboard!
        let nextView = storyboard.instantiateViewController(withIdentifier: "Main")
        present(nextView, animated: true, completion: nil)
    }
    
    //알림창 띄우기
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        self.present(alert, animated: true)
    }
    
    
}
