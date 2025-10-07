#!/usr/bin/env python

import sys
import os
from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn
import socket

# Import local packages
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))

from ah_wrapper import AHSerialClient

SOCKET_IP = "127.0.0.1"
XMLRPC_PORT = 40405
UDP_IN_PORT = 65433
UDP_OUT_PORT = 65434

class Daemon:
	def __init__(self):
		# AH Wrapper client
		self.client = None
		
		# Socket variables
		self.in_sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		self.in_sock.settimeout(3)
		self.server = MultithreadedSimpleXMLRPCServer((SOCKET_IP, XMLRPC_PORT), allow_none=True)
		self.server.RequestHandlerClass.protocol_version = "HTTP/1.1"
		self.running = True

		# Register XMLRPC functions
		self.server.register_function(self.connect, "connect")
		self.server.register_function(self.disconnect, "disconnect")
		self.server.register_function(self.set_position, "set_position")
		self.server.register_function(self.set_grip, "set_grip")
		self.server.register_function(self.set_torque, "set_torque")

	def main(self):
		self.server.serve_forever()

	def connect(self, baud, simulated):

		if bool(simulated):
			# Simulated hand has basic position functionaly for setting and getting
			self.client = AHSerialClient(simulated=True)
			return True
		
		if baud:
			self.client = AHSerialClient(baud_rate=baud)
		else:
			self.client = AHSerialClient()

		# Program will exit if hand does not connect
		return True


	def disconnect(self):
		if self.client:
			self.client.close()
		return True

	def set_position(self, positions):
		if self.client:
			positions = [float(p) for p in positions]
			positions[-1] = positions[-1] * -1
			self.client.set_position(positions)
		return True

	def set_grip(self, raw_grip, speed=255):
		speed = int(speed)
		if self.client:
			try:
				self.client.set_grip(raw_grip, speed)
			except KeyError:
				sys.stdout.write("Invalid Key!!")
				pass
			finally:
				return True

	def set_torque(self, torques):
		if self.client:
			torques = [float(t)/100 for  t in torques]
			torques[-1] = torques[-1] * -1
			self.client.set_torque(torques)
		return True


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass


if __name__ == "__main__":
	daemon = Daemon()
	daemon.main()


