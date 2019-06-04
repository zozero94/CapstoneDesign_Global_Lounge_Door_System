//
//  LogOnViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit
import SwiftSocket
import CoreLocation
import AudioToolbox

class LogOnViewController: UIViewController {
    @IBOutlet var qrImage: UIImageView!
    @IBOutlet var userImage: UIImageView!

    @IBOutlet var nameLabel: UILabel!
    @IBOutlet var studentIdLabel: UILabel!
    @IBOutlet var departmentLabel: UILabel!
    @IBOutlet var collegeLabel: UILabel!
    
    
    let rsa: RSAWrapper? = RSAWrapper()
    let convertor : Convertor? = Convertor()
    let client: ServerConnection = ServerConnection.sharedInstance
   
    var imageUrl : String? = ""
    var studentStringData : String? = ""
    var studentDicData : [String : String] = [:]
    var qrData : String? = ""
    var qrDataDic : [String : String] = [:]
    var isCaptured: Bool = false
    var loc = -1
    var locationManager: CLLocationManager!
    
    var beaconPeripheralData: NSDictionary!
    
    var appConfiguredBrightness: CGFloat = 0.0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        setImageView(image: qrImage)
        setImageView(image: userImage)
        
        setLabel(label: nameLabel)
        setLabel(label: studentIdLabel)
        setLabel(label: departmentLabel)
        setLabel(label: collegeLabel)
        
        self.screenshotTaken()
        
        self.appConfiguredBrightness = UIScreen.main.brightness
        
        studentStringData = UserDefaults.standard.string(forKey: "qrData")!
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        qrData = client.sendData2(data: "{\"seqType\":\"200\"}\n")
        studentDicData = convertor!.convertStringToDic(jsonString: studentStringData!)
        
        qrDataDic = convertor!.convertStringToDic(jsonString: qrData!)
        
        if self.appConfiguredBrightness != 1.0 {
            UIScreen.main.brightness = 1.0
        }
        
        if UserDefaults.standard.string(forKey: "id")! != "admin"{
            imageUrl = "https://udream.sejong.ac.kr/upload/per/" + studentDicData["studentID"]! + ".jpg?ver=20190515205000"
        } else {
            let adminImg = self.client.sendData2(data: "{\"seqType\":\"203\"}\n")
            var imgDic = convertor!.convertStringToDic(jsonString: adminImg)
            imageUrl = imgDic["img"]
        }
        
        qrImage.image = generateQRCode(from: qrDataDic["qr"]!)
        
        nameLabel.text = "  이름  | " + studentDicData["name"]!
        studentIdLabel.text = "  학번  | " + studentDicData["studentID"]!
        departmentLabel.text = "  학과  | " + studentDicData["department"]!
        collegeLabel.text = "  단과대학  | " + studentDicData["college"]!
        
        userImage.image = getUrlImage(urlString: imageUrl!)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        
        UIScreen.main.brightness = self.appConfiguredBrightness
    }
    
    func screenshotTaken(){
        NotificationCenter.default.addObserver(forName: UIApplication.userDidTakeScreenshotNotification, object: nil, queue: OperationQueue.main){ notification in
            self.showAlert(Message: "캡쳐 시 QR코드 사용이 제한됩니다.")
            _ = self.client.sendData2(data: "{\"seqType\":\"201\"}\n")
        }
    }
    
    @IBAction func logoutBtn(_ sender: UIButton) {
        UserDefaults.standard.set("LOGOUT",forKey: "logout")
        UserDefaults.standard.synchronize()
        _ = self.client.sendData2(data: "{\"seqType\":\"500\"}\n")
        client.close()
        dismiss(animated: true, completion: nil)
    }
    
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
    
    func getUrlImage(urlString : String) -> UIImage? {
        let url = URL(string: urlString)
        do {
            let data = try Data(contentsOf: url!)
            let image = UIImage(data: data)
            
            return image
            
        }catch let err {
            print("Error : \(err.localizedDescription)")
            
            return nil
        }
    }
    override func motionBegan(_ motion: UIEvent.EventSubtype, with event: UIEvent?) {
        if UserDefaults.standard.string(forKey: "id") == "admin" {
            
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
            
            if loc > 0 {
                let _ = self.client.sendData(data: "{\"seqType\": \"205\"}" + "\n")
            }
            else {
                self.showAlert(Message: "출입문과의 거리 인식이 불안정합니다.")
            }

        }
    }
    
    
    func setImageView(image: UIImageView) {
        image.layer.borderColor = UIColor.black.cgColor
        image.layer.borderWidth = 1.0
        image.layer.cornerRadius = 10
    }
    
    func setLabel(label : UILabel) {
        label.layer.borderWidth = 1.0
        label.layer.borderColor = UIColor.gray.cgColor
        label.layer.cornerRadius = 4.0
    }
    
    func showAlert(Message: String) {
        let alert = UIAlertController(title: "", message: Message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: nil ))
        
        self.present(alert, animated: true)
    }
}

