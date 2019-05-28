import threading, time
import data
import sensor as sensor

lock = threading.Lock()
eve = threading.Event()

class MainMovement(threading.Thread):


    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket


    def run(self):

        while True:

            try:

                res = self.client_socket.recv(1024)
                res_dic = data.toDict(res)
                print(res)

                lock.acquire()
                if res_dic['seqType'] == "301":
                    if sensor.first_infrared(time.time()):
                        self.client_socket.send(data.toString(400))
                    else:
                        self.client_socket.send(data.toString(401))

                elif res_dic['seqType'] == "302":
                    time.sleep(1)
        
                elif res_dic['seqType'] == "700":
                    sensor.first_infrared(time.time())
                    self.client_socket.send(data.toString(402))
                lock.release()
                eve.set()

            except Exception as e:

                print (e)

 

 

class QrMovement(threading.Thread):


    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket

    def run(self):
        
        eve.set()
        while True:

            try:
                code = data.toStringQr()
                sensor.buzzer()

                self.client_socket.send(code.encode())
                eve.clear()
                eve.wait()

            except Exception as e:

                print (e)
