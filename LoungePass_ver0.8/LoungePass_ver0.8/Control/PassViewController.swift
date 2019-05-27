//
//  PassViewController.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import UIKit


class PassViewController: UIViewController {
    
    @IBOutlet var qrImageViewBorder: UIImageView!
    @IBOutlet var qrImageView: UIImageView!
    @IBOutlet var userImageViewBorder: UIImageView!
    @IBOutlet var userImageView: UIImageView!
    
    @IBOutlet var nameTextField: UITextField!
    @IBOutlet var studentIdTextField: UITextField!
    
    @IBOutlet var departmentTextField: UITextField!
    @IBOutlet var collegeTextField: UITextField!
    
    
    let imageGenerator = ImageGenerator()
    private let control = PassViewControl()
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setImageView(image: qrImageViewBorder)
        setImageView(image: userImageViewBorder)
        UIScreen.main.brightness = CGFloat(1.0)
        
        _ = self.control.requestImage(key: "img")
        _ = self.control.requestImage(key: "qr")
        DispatchQueue.global(qos: .userInitiated).async {
            // 소켓 response 대기
            //while
            while true {
                let response = self.control.IsResponse()
                
                if response != nil {
                    // response 후 데이터에따라 뷰에 뿌려주기
                    DispatchQueue.main.async {
                        switch response!["seqType"] as! String {
                        case ServerConstant().STATE_CREATE : self.qrImageView.image = response!["qr"] as? UIImage
                        case ServerConstant().STATE_URL : self.userImageView.image = response!["img"] as? UIImage
                        default: break
                        }
                    }
                }else {
                    DispatchQueue.main.async {
                        self.showAlert(Message: "서버와의 연결이 불안정합니다.")
                    }
                }
            }
        }
        
        nameTextField.text = self.control.getStudentInfo(key: "name")
        studentIdTextField.text = self.control.getStudentInfo(key: "studentID")
        departmentTextField.text = self.control.getStudentInfo(key: "department")
        collegeTextField.text = self.control.getStudentInfo(key: "college")
        
    }
    
    @IBAction func logoutClick(_ sender: UIButton) {
        //UIScreen.main.brightness = CGFloat(UserDefaults.standard.object(forKey: "brightness") as! Float)
        UIScreen.main.brightness = CGFloat(UserDefaults.standard.object(forKey: "brightness") as! Float)
        self.control.logoutClicked()
        present()
    }
    
    func setImageView(image: UIImageView) {
        image.layer.borderColor = UIColor.black.cgColor
        image.layer.borderWidth = 0.5
        image.layer.cornerRadius = 10
    }
    
    func present() {
        let storyboard: UIStoryboard = self.storyboard!
        let nextView = storyboard.instantiateViewController(withIdentifier: "main")
        present(nextView, animated: true, completion: nil)
    }
    
    //알림창 띄우기
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        self.present(alert, animated: true)
    }
    
}
