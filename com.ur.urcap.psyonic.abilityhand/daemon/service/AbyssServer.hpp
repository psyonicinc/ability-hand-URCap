#pragma once

#include <vector>
#include <xmlrpc-c/registry.hpp>

#include "AbilityHandData.hpp"

/**
 * @brief xmlrpc-c Abyss server for XML-RPC communication.
 * See: http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server_abyss++.html
 */
class AbyssServer {

  public:
    AbyssServer(AbilityHandData* data);
    virtual ~AbyssServer();
    static void* run(void* aserver);

  private:
    AbilityHandData* data;
    xmlrpc_c::registry serviceRegistry;
};

