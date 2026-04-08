#!/usr/bin/env bash

set -euo pipefail

sed -i "/^(def version/c\
(def version \"$1\")" src/hkimjp/jpy/view.clj
