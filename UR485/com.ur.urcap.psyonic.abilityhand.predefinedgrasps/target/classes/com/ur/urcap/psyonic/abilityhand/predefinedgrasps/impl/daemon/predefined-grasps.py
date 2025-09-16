#!/usr/bin/env python

import sys
import os
import time

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))
import serial
from ah_wrapper import AHSerialClient

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

byte_grip_dict = {"Open": 0x00, 
                  "Power": 0x01,
                  "Key": 0x02,
                  "Pinch": 0x03,
                  "Tripod Opened": 0x04,
                  "Sign of the Horns": 0x05,
                  "Cylinder": 0x06,
                  "Mouse Grasp": 0x07,
                  "Power/Key Switch": 0x08,
                  "Point": 0x09,
                  "Rude...": 0x0A,
                  "Hook": 0x0B,
                  "Relax": 0x0C,
                  "Sleeve": 0x0D,
                  "Peace": 0x0E,
                  "Tripod Closed": 0x0F,
                  "Hang Loose": 0x10,
                  "Handshake": 0x11,
                  "Fixed Pinch": 0x12}
byte_grip = {"choice": None}

def isReachable():
	return True


def get_grasp(choice):
    sys.stdout.write("Grasp choice:" + str(choice) + "\n")
    byte_grip['choice'] = byte_grip_dict[str(choice)]

    return byte_grip


def send_grasp():
    client = AHSerialClient()
    try:
        time.sleep(500/client.rate_hz)
        client.set_grip(byte_grip['choice'])
        time.sleep(250/client.rate_hz)
    except KeyboardInterrupt:
          pass
    finally:
        client.close()
    return "True"

sys.stdout.write("MyDaemon daemon1 started")
sys.stderr.write("MyDaemon daemon2 started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 49156))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(get_grasp, "get_grasp")

server.register_function(send_grasp, "send_grasp")
server.serve_forever()
