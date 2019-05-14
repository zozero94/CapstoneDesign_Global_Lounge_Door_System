import threading, time
import data
import movement as mv

lock = threading.Lock()
eve = threading.Event()

class Move(threading.Thread):


    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket


    def run(self):

        while True:

            try:

                res = self.client_socket.recv(1024)
                res_dic = data.toDict(res)

               
                if res_dic['seqType'] == 301:

                    lock.acquire()
                    if mv.a_distance(time.time()):
                        self.client_socket.send(data.toString(400))
                        self.client_socket.send(b'\n')
                        print('400')

                    else:
                        self.client_socket.send(data.toString(401))
                        self.client_socket.send(b'\n')
                        print('401')

                    print("openDoor:301")

                    lock.release()
                    eve.set()

                elif res_dic['seqType'] == 302:

                    lock.acquire()
                    print('restart')
                    time.sleep(1)
                    lock.release()
                    eve.set()

                    continue

                elif res_dic['seqType'] == 700:

                    lock.acquire()
                    print("openDoor:700")
                    mv.a_distance(time.time())
                    self.client_socket.send(data.toString(402))
                    self.client_socket.send(b'\n')

                    lock.release()
                    eve.set()

            except Exception as e:

                print (e)

 

 

class Qr(threading.Thread):


    def __init__(self, client_socket):

        threading.Thread.__init__(self)
        self.client_socket = client_socket

    def run(self):
        
        eve.set()
        while True:

            try:

                code = data.toStringQr()

                self.client_socket.send(code.encode())
                self.client_socket.send(b'\n')
                eve.clear()
                eve.wait()
        
 

            except Exception as e:

                print (e)