from socket import *
import my_thread as th
import data


HOST = '192.168.0.7'
PORT = 5050
BUFSIZE = 1024
ADDR = (HOST, PORT)

client_socket = socket(AF_INET, SOCK_STREAM)
client_socket.connect(ADDR)
client_socket.send(data.toString(601))
client_socket.send(b'\n')
    
m = th.Move(client_socket)
q = th.Qr(client_socket)

m.start()
q.start()