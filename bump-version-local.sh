#!/usr/bin/env bash

set -eux

gsed -i "/^(def version/c\
(def version \"$1\")" src/hkimjp/jpy/view.clj
