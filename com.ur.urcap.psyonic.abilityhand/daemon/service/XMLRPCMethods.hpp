#pragma once

#include <xmlrpc-c/base.hpp>
#include <xmlrpc-c/registry.hpp>

#include "AbilityHandData.hpp"

/**
 * Add a XMLRPC adapter (i.e. Hello) for each method that should be exposed through XML-RPC.
 * Relevant XMLRPC-C documentation can be found here: http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html
 */

class IsReachable : public xmlrpc_c::method
{
public:
    IsReachable(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);

private:
    IsReachable(); // Hereby disabled
    AbilityHandData* data;
};

class startPositionThread : public xmlrpc_c::method {
  public:
    startPositionThread(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    startPositionThread();
    AbilityHandData* data;
};

class stopPositionThread : public xmlrpc_c::method {
  public:
    stopPositionThread(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    stopPositionThread();
    AbilityHandData* data;
};

class startGripThread : public xmlrpc_c::method {
  public:
    startGripThread(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    startGripThread();
    AbilityHandData* data;
};

class stopGripThread : public xmlrpc_c::method {
  public:
    stopGripThread(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    stopGripThread();
    AbilityHandData* data;
};

class setPosition: public xmlrpc_c::method {
  public:
    setPosition(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    setPosition(); // Hereby disabled
    AbilityHandData* data;
};

class setGrip: public xmlrpc_c::method {
  public:
    setGrip(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    setGrip(); // Hereby disabled
    AbilityHandData* data;
};

class setTorque: public xmlrpc_c::method {
  public:
    setTorque(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    setTorque(); // Hereby disabled
    AbilityHandData* data;
};

class setDuty: public xmlrpc_c::method {
  public:
    setDuty(AbilityHandData* data);
    void execute(xmlrpc_c::paramList const &paramList, xmlrpc_c::value *const retvalP);
  private:
    setDuty(); // Hereby disabled
    AbilityHandData* data;
};
