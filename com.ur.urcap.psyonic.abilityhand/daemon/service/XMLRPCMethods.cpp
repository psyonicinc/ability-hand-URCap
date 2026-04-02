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



startPositionThread::startPositionThread(AbilityHandData* data) : data(data)
{
  this->_signature = "b:";
  this->_help = "Starts position thread";
}
void startPositionThread::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  paramList.verifyEnd(0);
  bool result = data->startPositionThread();
  *retvalP = xmlrpc_c::value_boolean(result);
}



stopPositionThread::stopPositionThread(AbilityHandData* data) : data(data)
{
  this->_signature = "b:";
  this->_help = "stops position thread";
}
void stopPositionThread::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  paramList.verifyEnd(0);
  bool result = data->stopPositionThread();
  *retvalP = xmlrpc_c::value_boolean(result);
}



startGripThread::startGripThread(AbilityHandData* data) : data(data)
{
  this->_signature = "b:";
  this->_help = "Starts Grip thread";
}
void startGripThread::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  paramList.verifyEnd(0);
  bool result = data->startGripThread();
  *retvalP = xmlrpc_c::value_boolean(result);
}



stopGripThread::stopGripThread(AbilityHandData* data) : data(data)
{
  this->_signature = "b:";
  this->_help = "stops Grip thread";
}
void stopGripThread::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  paramList.verifyEnd(0);
  bool result = data->stopGripThread();
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

setGrip::setGrip(AbilityHandData* data) : data(data)
{
  this->_signature = "b:ii";
  this->_help = "Set hand grip mode";
}
void setGrip::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value* const retvalP) {
  paramList.verifyEnd(2); // 2 params: grip and speed

  int cmd_grip = paramList.getInt(0);
  int speed = paramList.getInt(1);

  bool result = data->setGrip(static_cast<uint8_t>(cmd_grip),
                              static_cast<uint8_t>(speed));

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
