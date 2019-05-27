//
//  ImageGenerator.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation
import UIKit

class ImageGenerator{
    
    
    func getQRCode(from string: String) -> UIImage? {
        let data = string.data(using: String.Encoding.ascii)
        
        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 3, y: 3)
            
            if let output = filter.outputImage?.transformed(by: transform) {
                return UIImage(ciImage: output)
            }
        }
        
        return nil
    }
    
    //url -> image
    func getUrlImage(urlString:String)->UIImage?{
        let url = URL(string: urlString)
        let session = URLSession(configuration: .default)
        var Img :UIImage?
        
        //creating a dataTask
        let group = DispatchGroup.init()
        let queue = DispatchQueue.global()
        group.enter()
        queue.async {
            let getImageFromUrl = session.dataTask(with: url!) { (data, response, error) in
                if let e = error {  print("Error Occurred: \(e)") }
                else {
                    
                    if (response as? HTTPURLResponse) != nil {
                        if let imageData = data {
                            Img = UIImage(data: imageData)
                            group.leave()
                        }
                        else { print("Image file is currupted") }
                    }
                    else {print("No response from server") }
                }
            }
            //starting the download task
            getImageFromUrl.resume()
            
        }
        _ = group.wait(timeout: .distantFuture)
        
        return Img
    }
    
}
