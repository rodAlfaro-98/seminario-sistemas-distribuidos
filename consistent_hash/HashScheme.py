#! /usr/bin/env python3
# -*- coding: utf-8 -*-
# vim:fenc=utf-8
#
# Copyright © 2022 rzavalet <rzavalet@noemail.com>
#
# Distributed under terms of the MIT license.

from abc import ABC, abstractmethod
"""
This is an abstract class for Hash schemes.
"""
class HashScheme(ABC):

    @abstractmethod
    def dump(self):
        pass

    @abstractmethod
    def add_node(self, new_node):
        pass

    @abstractmethod
    def remove_node(self, node):
        pass

    @abstractmethod
    def hash(self, value):
        pass

