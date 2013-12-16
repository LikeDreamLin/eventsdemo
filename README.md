eventsdemo
==========

Demo implementations of 
* events handling using JDK Obeservable, Spring ApplicationListener, Camel, ...
* state machines/state flow implementations

Event Handling
--------------
To learn, verify and decide on event mechanisms like publish/subscribe, simple tests for 
* JDK Obeservable
* Spring ApplicationListener
* Camel
are provided.

State Handling
--------------
To learn, verify and decide on state mechanisms, e.g. state transition definition, execution and actions, simple test for:
* [bbv state machine](https://code.google.com/p/bbvcommon/wiki/StateMachine)
* [stateless4j](https://code.google.com/p/stateless4j/)
* [EasyFlow](https://github.com/Beh01der/EasyFlow)
* implementation based on com.google.common.collect.Multimap

Camel EIP examples
------------------
As Camel was defined to be used for Event Handling further examples have been added in the events-camel module:
* Publish/Subscribe to several consumers (seda and jms)
* Enriching the headers with a thread local Tenant information
* Sending between Camel contexts using the vm component (seda only within a Camel context)
* Routing using RoutingSlip 
* Sending after transaction commit using Springs TransactionSynchronizationAdapter.
* Distributed transactions between DB(H2), Camel and ActiveMQ using Bitronix.
* Scatter/Gather example (delete or not to delete - that is the question).



