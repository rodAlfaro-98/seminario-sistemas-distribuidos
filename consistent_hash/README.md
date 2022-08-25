# A minimal implementation of a Consistent Hash

Consistent Hashing is a distributed hashing scheme that operates independently
of the number of servers or objects in a distributed hash
table[[1](https://www.toptal.com/big-data/consistent-hashing)]. The idea was
introduced in a paper from 1997: [Consistent hashing and random trees:
distributed caching protocols for relieving hot spots on the World Wide
Web](https://www.cs.princeton.edu/courses/archive/fall09/cos518/papers/chash.pdf)

The idea is to map both servers and objects to a ring (circle). Objects are
associated to the closest server in clockwise (or counterclockwise) direction.
This scheme is specially useful in situation where servers leave or are added to
the system, because only a bounded number of objects need to be rehashed to a
new server.

## Sample code

In this repository we present a quick implementation of the idea of Consistent
Hash (weight factor of 1).

To execute the program:

`$ python3 test.py`


## Exercise

Please complete the implementation of the Modulo hash class (ModHash.py).
Instrument both hash classes to count the number of objects that need to be
migrated when a server is added and when a server is removed. Which hash
strategy behaves better?
