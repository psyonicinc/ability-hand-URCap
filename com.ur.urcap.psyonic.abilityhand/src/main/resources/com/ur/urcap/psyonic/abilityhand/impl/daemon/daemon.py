#!/usr/bin/env python

import sys
import os
from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn
import socket
import time

# Import local packages
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))

from ah_wrapper import AHSerialClient

SOCKET_IP = "127.0.0.1"
XMLRPC_PORT = 40405
# UDP_IN_PORT = 65433
# UDP_OUT_PORT = 65434

class Daemon:
	def __init__(self):
		# AH Wrapper client
		self.client = None
		
		# Socket variables
		try:
			# self.in_sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
			# self.in_sock.settimeout(3)
			self.server = MultithreadedSimpleXMLRPCServer((SOCKET_IP, XMLRPC_PORT), allow_none=True)
			self.server.RequestHandlerClass.protocol_version = "HTTP/1.1"
			# self.running = True
			self.server.socket.settimeout(5)

			# Register XMLRPC functions
			self.server.register_function(self.connect, "connect")
			self.server.register_function(self.disconnect, "disconnect")
			self.server.register_function(self.set_position, "set_position")
			self.server.register_function(self.set_grip, "set_grip")
		except Exception as e:
			sys.stdout.write("daemon init fail\n")
			sys.stdout.write(str(e))
		
	def main(self):
		try:
			sys.stdout.write("Starting Ability Hand Serve Forever\n")
			self.server.serve_forever()
		except Exception as e:
			sys.stdout.write("main fail\n")
			sys.stdout.write(str(e))
			self.client.close()


	def connect(self, baud, simulated):

		try:
			if bool(simulated):
			# Simulated hand has basic position functionaly for setting and getting
				self.client = AHSerialClient(simulated=True)
			elif baud:
				self.client = AHSerialClient(baud_rate=baud)
			else:
				self.client = AHSerialClient()
		except Exception as e:
			sys.stderr.write("AH connection error\n")
			sys.stdout.write(str(e))
			return False
		# Program will exit if hand does not connect
		return True

		
	def disconnect(self):
		if self.client:
			try:
				self.client.close()
			except Exception as e:
				sys.stderr.write("disconnect error\n")
				sys.stderr.write(str(e))
			return True
		else:
			return True


	def set_position(self, positions):
		if self.client:
			try:
				positions = [float(p) for p in positions]
				positions[-1] = positions[-1] * -1
				self.client.set_position(positions)
			except Exception as e:
				sys.stderr.write("position error\n")
				sys.stdout.write(str(e))
				self.client.close()
			return True
		else:
			return True


	def set_grip(self, raw_grip, speed=255):
		if self.client:
			try:
				speed = int(speed)
				self.client.set_grip(raw_grip, speed)
			except Exception as e:
				sys.stderr.write("grip error\n")
				sys.stdout.write(str(e))
				self.client.close()
			return True
		else:
			return True



class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	daemon_threads = True
	allow_reuse_address = True
	request_queue_size = 10
	


if __name__ == "__main__":
	daemon = Daemon()
	daemon.main()


