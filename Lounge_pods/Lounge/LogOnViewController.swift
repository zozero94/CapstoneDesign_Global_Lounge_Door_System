//
//  LogOnViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit
import SwiftSocket

class LogOnViewController: UIViewController {
    @IBOutlet var qrImage: UIImageView!
    @IBOutlet var userImage: UIImageView!
    
    let rsa: RSAWrapper? = RSAWrapper()
    
    let string = "{\"seqType\": \"203\"}" + "\n"
    
    let client: ServerConnection = ServerConnection.sharedInstance
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setImageView(image: qrImage)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        qrImage.image = generateQRCode(from: "rkskekfk")
        client.sendData2(data: string)
    }
    
    
    
    @IBAction func logoutBtn(_ sender: UIButton) {
        print("dismiss--------")
        UserDefaults.standard.set("LOGOUT",forKey: "logout")
        UserDefaults.standard.synchronize()
        client.close()
        dismiss(animated: true, completion: nil)
    }
    
    func generateQRCode(from string: String) -> UIImage?{
        print("QRCode 이미지 생성")
        let data = string.data(using: String.Encoding.ascii)
        
        if let filter = CIFilter(name: "CIQRCodeGenerator"){
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 2, y: 2)
        
            if let output = filter.outputImage?.transformed(by: transform){
                return UIImage(ciImage: output)
            }
        }
        return nil
    }
    
    func setImageView(image: UIImageView) {
        qrImage.layer.borderColor = UIColor.black.cgColor
        qrImage.layer.borderWidth = 1.0
        qrImage.layer.cornerRadius = 10
    }
}
