#include "AbyssServer.hpp"

#include <memory>
#ifndef PSTREAM
#include <xmlrpc-c/server_abyss.hpp>
#endif

#include "XMLRPCMethods.hpp"

using namespace std;

// Please use a unique port number!
#define SERVER_PORT 40405

AbyssServer::AbyssServer(AbilityHandData* data) :
    data(data)
{
  // Add all adapters defined in XMLRPCMethods here
  serviceRegistry.addMethod("isReachable", new IsReachable(data));
  serviceRegistry.addMethod("setPosition", new setPosition(data));
  serviceRegistry.addMethod("setTorque", new setTorque(data));
  serviceRegistry.addMethod("setDuty", new setDuty(data));
}

AbyssServer::~AbyssServer() {
}

void* AbyssServer::run(void* aserver) {

  AbyssServer* ptr = static_cast<AbyssServer*>(aserver);

  xmlrpc_c::serverAbyss server(
      xmlrpc_c::serverAbyss::constrOpt().
      portNumber(SERVER_PORT).
      registryP(&(ptr->serviceRegistry)).
      keepaliveMaxConn(UINT_MAX)
    );

  server.run();

  return NULL;
}
