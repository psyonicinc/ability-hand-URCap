#include "XMLRPCMethods.hpp"

#include <array>
#include <stdexcept>

#include <iostream>

using namespace std;

IsReachable::IsReachable(AbilityHandData* data) : data(data) 
{
    this->_signature = "b:"; // RPC method signature, which is not mandatory for basic operation, see
    this->_help= "Returns if daemon is reachable or not!";
                              // http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html#howto
}

void IsReachable::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
    paramList.verifyEnd(0);
    bool result = data->isReachable();
    *retvalP = xmlrpc_c::value_boolean(result);
}

setPosition::setPosition(AbilityHandData* data) : data(data)
{
  this->_signature = "b:A"; // RPC method signature, which is not mandatory for basic operation, see http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html#howto
  this->_help = "Set hand joint positions";
}

void setPosition::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  
  paramList.verifyEnd(1);   // exactly one param: the array

    xmlrpc_c::value_array arrVal(paramList.getArray(0));
    std::vector<xmlrpc_c::value> const elems(arrVal.vectorValueValue());

    if (elems.size() != 6) {
        throw std::runtime_error("setPosition expects an array of 6 doubles");
    }

    std::array<float, 6> cmd = {
        static_cast<float>(xmlrpc_c::value_double(elems[0])),
        static_cast<float>(xmlrpc_c::value_double(elems[1])),
        static_cast<float>(xmlrpc_c::value_double(elems[2])),
        static_cast<float>(xmlrpc_c::value_double(elems[3])),
        static_cast<float>(xmlrpc_c::value_double(elems[4])),
        static_cast<float>(xmlrpc_c::value_double(elems[5]))
    };

    bool result = data->setPosition(cmd);
    *retvalP = xmlrpc_c::value_boolean(result);
}


setTorque::setTorque(AbilityHandData* data) : data(data)
{
  this->_signature = "b:A"; // RPC method signature, which is not mandatory for basic operation, see http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html#howto
  this->_help = "Set hand joint torques";
}

void setTorque::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  
  paramList.verifyEnd(1);   // exactly one param: the array

    xmlrpc_c::value_array arrVal(paramList.getArray(0));
    std::vector<xmlrpc_c::value> const elems(arrVal.vectorValueValue());

    if (elems.size() != 6) {
        throw std::runtime_error("setPosition expects an array of 6 doubles");
    }

    std::array<float, 6> cmd = {
        static_cast<float>(xmlrpc_c::value_double(elems[0])),
        static_cast<float>(xmlrpc_c::value_double(elems[1])),
        static_cast<float>(xmlrpc_c::value_double(elems[2])),
        static_cast<float>(xmlrpc_c::value_double(elems[3])),
        static_cast<float>(xmlrpc_c::value_double(elems[4])),
        static_cast<float>(xmlrpc_c::value_double(elems[5]))
    };

    bool result = data->setPosition(cmd);
    *retvalP = xmlrpc_c::value_boolean(result);
}


setDuty::setDuty(AbilityHandData* data) : data(data)
{
  this->_signature = "b:A"; // RPC method signature, which is not mandatory for basic operation, see http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html#howto
  this->_help = "Set hand joint duty";
}

void setDuty::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  
  paramList.verifyEnd(1);   // exactly one param: the array

    xmlrpc_c::value_array arrVal(paramList.getArray(0));
    std::vector<xmlrpc_c::value> const elems(arrVal.vectorValueValue());

    if (elems.size() != 6) {
        throw std::runtime_error("setPosition expects an array of 6 doubles");
    }

    std::array<float, 6> cmd = {
        static_cast<float>(xmlrpc_c::value_double(elems[0])),
        static_cast<float>(xmlrpc_c::value_double(elems[1])),
        static_cast<float>(xmlrpc_c::value_double(elems[2])),
        static_cast<float>(xmlrpc_c::value_double(elems[3])),
        static_cast<float>(xmlrpc_c::value_double(elems[4])),
        static_cast<float>(xmlrpc_c::value_double(elems[5]))
    };

    bool result = data->setPosition(cmd);
    *retvalP = xmlrpc_c::value_boolean(result);
}
