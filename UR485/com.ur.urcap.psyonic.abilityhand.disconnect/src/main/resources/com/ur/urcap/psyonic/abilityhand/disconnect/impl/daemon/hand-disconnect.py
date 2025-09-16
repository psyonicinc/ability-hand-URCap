#!/usr/bin/env python


import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))
import serial
from ah_wrapper import AHSerialClient

import time
import socket
import threading
import json

HOST='127.0.0.1'
PORT=12345

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn


def isReachable():
    return True


def start_END_socket_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client_socket.connect((HOST, PORT))
        print('Connected to server @', HOST)
        message = ['E']
        json_message = json.dumps(message)
        client_socket.sendall(json_message.encode('utf-8'))

    except socket.error as e:
        print("Connection Refused!!", e)
    except Exception as e:
        print("An error has occured:", e)

sys.stderr.write("MyDaemon22 daemon started")


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 60100))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(start_END_socket_client, "start_END_socket_client")
server.serve_forever()