#include "AbilityHandMain.hpp"

#include <iostream>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>

#include "AbyssServer.hpp"
#include "Data.hpp"

using namespace std;

bool AbilityHandMain::quit = false;
int AbilityHandMain::exit_value = 0;

// called at exit
void AbilityHandMain::shutdown() {
  cerr << "AbilityHandMain::shutdown" << endl;
}

// Handle various signals
void AbilityHandMain::handler(int signum) {

  if(signum == SIGINT) {
    cerr << "AbilityHandMain::handler caught CTRL-C" << endl;
    AbilityHandMain::quit = true;
  } else {
    cerr << "AbilityHandMain::handler caught signal: " << signum << endl;
  }
}

int main(int argc, char *argv[]) {

  // call handler() at CTRL-C (signum = SIGINT)
  if(signal(SIGINT, SIG_IGN) != SIG_IGN) {
    signal(SIGINT, AbilityHandMain::handler);
  }

  // call shutdown() at program exit
  atexit(AbilityHandMain::shutdown);

  // Example data container for domain logic
  AbilityHandData data;

  // To communicate between URScript and the executable we use the xmlrpc-c library
  // This library is standard available on the robot and in the development toolchain.
  AbyssServer gui(&data);

  pthread_t thread_id;
  if(pthread_create(&thread_id, NULL, gui.run, &gui)){
    cerr << "Couldn't create pthread" << endl;
    return EXIT_FAILURE;
  }

  cout << "Ability Hand started" << endl;

  pthread_join(thread_id, NULL);

  cout << "Ability Hand stopped" << endl;

  return AbilityHandMain::exit_value;
}
