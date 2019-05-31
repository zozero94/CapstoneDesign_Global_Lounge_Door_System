//
//  beacon.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 30/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import UIKit
import CoreLocation

extension PassViewController : CLLocationManagerDelegate{
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedAlways {
            if CLLocationManager.isMonitoringAvailable(for: CLBeaconRegion.self) {
                if CLLocationManager.isRangingAvailable() {
                    startScanning()
                }
            }
        }
    }
    
    func startScanning() {
        let uuid = UUID(uuidString: "E20A39F4-73F5-4BC4-A12F-17D1AD07A961")!
        let beaconRegion = CLBeaconRegion(proximityUUID: uuid, major: 1, minor: 1, identifier: "MyBeacon")
        
        locationManager.startMonitoring(for: beaconRegion)
        locationManager.startRangingBeacons(in: beaconRegion)
    }
    
    func locationManager(_ manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], in region: CLBeaconRegion) {
        if beacons.count > 0 {
            updateDistance(beacons[0].proximity)
        } else {
            updateDistance(.unknown)
        }
    }
    
    func updateDistance(_ distance: CLProximity) {
        UIView.animate(withDuration: 0.8) {
            switch distance {
            case .unknown:
                self.test = -1
                
            case .far:
                self.test = 0
               
            case .near:
                self.test = 1
                
            case .immediate:
                self.test = 2
                
            }
        }
    }
    
}
