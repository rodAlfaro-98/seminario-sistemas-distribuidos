#! /usr/bin/env python3
# -*- coding: utf-8 -*-
# vim:fenc=utf-8
#
# Copyright © 2022 rzavalet <rzavalet@noemail.com>
#
# Distributed under terms of the MIT license.


"""
This class represents a Resource (or a record) that is stored in one of the
nodes of the datastore.
"""
class Resource:
    def __init__(self, name):
        self.name = name


"""
This class represents a Node, the place were resources (or records) are stored
in the datastore.
"""
class Node:
    def __init__(self, name):
        self.name = name
        self.resources = []


"""
The class Store represents a distributed datastore. Each datastore stores a
number of resources. Resources are assigned to nodes based on a hash schema.
"""
class Store:
    def __init__(self, hash_generator):
        self.nodes = {}
        self.hash_generator = hash_generator

    def dump(self):
        """
        prints the contents of each of the nodes that make up the distributed
        datastore.
        """
        print("===== STORE =====")
        print("Using scheme: {0}".format(self.hash_generator.get_name()))
        self.hash_generator.dump()
        for node in self.nodes.keys():
            print('[{0} ({1} items)]'.format(self.nodes[node].name, len(self.nodes[node].resources)))
            for resource in self.nodes[node].resources:
                print('    - {0}'.format(resource))

    def add_node(self, new_node):
        migracion = 0
        """
        Creates a new node in the datastore. Once the node is created, a number
        of resources have to be migrated to conform to the hash schema.

        Important: Notice that this method is designed to work with a
        consistent hash. You may need to adjust it to make it work with modular
        hash. Hash_generator has a member "scheme_name" that you can use.
        """
        if(self.hash_generator.get_name() == "Consistent_Hash"):
            prev_node = self.hash_generator.hash(new_node)

            rc = self.hash_generator.add_node(new_node)
            if rc == 0:
                self.nodes[new_node] = Node(new_node)

                """
                If there is a node in the counter clockwise direction, then the
                resources stored in that node need to be rebalanced (removed from a
                node and added to another one).
                """
                if prev_node is not None:
                    resources = self.nodes[prev_node].resources.copy() 

                    for element in resources:
                        target_node = self.hash_generator.hash(element)

                        if target_node is not None and target_node != prev_node:
                            migracion += 1
                            self.nodes[prev_node].resources.remove(element)
                            self.nodes[target_node].resources.append(element)
        
        elif (self.hash_generator.get_name() == "Modular_Hash"):
            not_in_array = True

            for i in self.nodes.values():
                if(i.name == new_node):
                    not_in_array = False
                    break

            if(not_in_array):

                self.hash_generator.add_node(new_node)
                resources = []
                resources += [j for i in self.nodes.values() for j in i.resources]

                nodes_names = [i.name for i in self.nodes.values()]
                migracion = len(resources)
                nodes_names.append(new_node)

                self.nodes = {i:Node(nodes_names[i]) for i in range(len(nodes_names))}
                print("Keys: {0}".format(self.nodes.keys()))

                for i in resources:
                    self.add_resource(i)
        else: 
            pass
        return migracion

    def remove_node(self, node):
        """
        Removes a node from the datastore. When a node is removed, a number
        of resources (that were stored in that node) have to be migrated to
        conform to the hash schema.

        Important: Notice that this method is designed to work with a
        consistent hash. You may need to adjust it to make it work with modular
        hash. Hash_generator has a member "scheme_name" that you can use.
        """
        migracion = 0
        if(self.hash_generator.get_name() == "Consistent_Hash"):
            rc = self.hash_generator.remove_node(node)
            if rc == 0:
                for element in self.nodes[node].resources:
                    migracion += 1
                    self.add_resource(element)
                del self.nodes[node]
        elif (self.hash_generator.get_name() == "Modular_Hash"):
            in_array = False
            for i in self.nodes.values():
                print(i.name)
                if(i.name == node):
                    in_array = True
                    break
            print(in_array)

            if(in_array):

                self.hash_generator.remove_node(node)
                resources = []
                resources += [j for i in self.nodes.values() for j in i.resources]

                nodes_names = [i.name for i in self.nodes.values() if i.name != node]
                migracion = len(resources)

                print("Node names: {0}".format(nodes_names))
                self.nodes = {}
                key = 0
                for i in nodes_names:
                    self.nodes[key] = Node(i)
                    key+=1

                for i in resources:
                    self.add_resource(i)
                
        else:
            pass
        return migracion

    def add_resource(self, res):
        """
        Add a new resource (record) to the distributed datastore. The record is
        added to a node that is selected using the hash strategy.
        """
        target_node = self.hash_generator.hash(res)

        if target_node is not None:
            self.nodes[target_node].resources.append(res)
