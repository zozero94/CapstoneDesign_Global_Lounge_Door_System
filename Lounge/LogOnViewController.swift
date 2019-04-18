//
//  LogOnViewController.swift
//  Lounge
//
//  Created by An on 16/04/2019.
//  Copyright © 2019 An. All rights reserved.
//

import UIKit

class LogOnViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    @IBAction func logoutBtn(_ sender: UIButton) {
        print("dismiss--------")
        UserDefaults.standard.set("LOGOUT",forKey: "logout")
        UserDefaults.standard.synchronize()
        dismiss(animated: true, completion: nil)
    }
}
