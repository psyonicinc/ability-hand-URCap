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


finger_positions = {
    'index': 'blank',
    'middle': 'blank',
    'ring': 'blank',
    'pinky': 'blank',
    'thumb': 'blank',
    'thumb_rot': 'blank'
}


def get_index_flex(PCV):
    finger_positions['index'] = float(PCV)
    return float(PCV)

def get_middle_flex(PCV):
    finger_positions['middle'] = float(PCV)
    return float(PCV)

def get_ring_flex(PCV):
    finger_positions['ring'] = float(PCV)
    return float(PCV)

def get_pinky_flex(PCV):
    finger_positions['pinky'] = float(PCV)
    return float(PCV)

def get_thumb_flex(PCV):
    finger_positions['thumb'] = float(PCV)
    return float(PCV)

def get_thumb_rot(PCV):
    finger_positions['thumb_rot'] = float(PCV)
    return float(PCV)

def start_position_socket_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client_socket.connect((HOST, PORT))
        print('Connected to server @', HOST)
        message = ['P', finger_positions['index'], 
                   finger_positions['middle'], 
                   finger_positions['ring'], 
                   finger_positions['pinky'], 
                   finger_positions['thumb'], 
                   finger_positions['thumb_rot']]
        json_message = json.dumps(message)
        client_socket.sendall(json_message.encode('utf-8'))

    except socket.error as e:
        print("Connection Refused!!", e)
    except Exception as e:
        print("An error has occured:", e)


sys.stderr.write("MyDaemon daemon started")


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 60001))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(get_index_flex, "get_index_flex")
server.register_function(get_middle_flex, "get_middle_flex")
server.register_function(get_ring_flex, "get_ring_flex")
server.register_function(get_pinky_flex, "get_pinky_flex")
server.register_function(get_thumb_flex, "get_thumb_flex")
server.register_function(get_thumb_rot, "get_thumb_rot")
server.register_function(start_position_socket_client, "start_position_socket_client")
server.serve_forever()